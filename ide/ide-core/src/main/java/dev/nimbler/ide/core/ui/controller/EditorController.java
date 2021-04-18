package dev.nimbler.ide.core.ui.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.util.Strings;

import dev.nimbler.compiler.model.common.ISourceToken;
import dev.nimbler.ide.common.codeaccess.SourceFileInfo;
import dev.nimbler.ide.common.codeaccess.SourceParseAccess;
import dev.nimbler.ide.common.model.source.ISourceTokenProperties;
import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.ide.common.ui.SearchDirection;
import dev.nimbler.ide.common.ui.SearchScope;
import dev.nimbler.ide.common.ui.actions.contexts.ActionContext;
import dev.nimbler.ide.common.ui.config.TextEditorConfig;
import dev.nimbler.ide.common.ui.controller.EditorActions;
import dev.nimbler.ide.common.ui.controller.EditorsListener;
import dev.nimbler.ide.common.ui.keys.Key;
import dev.nimbler.ide.common.ui.keys.KeyLocation;
import dev.nimbler.ide.common.ui.keys.KeyMask;
import dev.nimbler.ide.common.ui.keys.QualifierKey;
import dev.nimbler.ide.core.ui.actions.contexts.source.SourceTokenContext;
import dev.nimbler.ide.core.ui.view.EditorSourceActionContextProvider;
import dev.nimbler.ide.core.ui.view.EditorView;
import dev.nimbler.ide.core.ui.view.KeyEventListener;
import dev.nimbler.ide.model.text.TextModel;
import dev.nimbler.ide.util.ui.text.StringText;
import dev.nimbler.ide.util.ui.text.Text;
import dev.nimbler.ide.util.ui.text.TextRange;

public final class EditorController implements EditorSourceActionContextProvider, EditorActions {

	private final EditorView editorView;
	private final TextEditorConfig config;
	private final EditorParseFileStateMachine parseStateMachine;

	private TextModel textModel;
	
	private SourceFileModel sourceFileModel;
	
	public EditorController(
			EditorView editorView,
			TextEditorConfig config,
			List<EditorsListener> listeners,
			TextModel textModel,
			SourceParseAccess sourceFilesModel,
			SourceFileInfo sourceFile,
			DelegatingSourceFileModel delegatingSourceFileModel) {

		Objects.requireNonNull(editorView);
		Objects.requireNonNull(config);
		
		this.editorView = editorView;
		this.config = config;
		this.textModel = textModel;

		this.parseStateMachine = new EditorParseFileStateMachine(sourceFile, sourceFilesModel);
		
		updateSourceFileModel(textModel, listeners, sourceFilesModel, sourceFile, delegatingSourceFileModel);
		
		editorView.addTextChangeListener((start, length, newText) -> {
			updateSourceFileModel(textModel, listeners, sourceFilesModel, sourceFile, delegatingSourceFileModel);
		});
		
		editorView.addKeyListener(new KeyEventListener() {
			
			@Override
			public boolean onKeyRelease(Key key, KeyMask mask, KeyLocation location) {
				return true;
			}
			
			@Override
			public boolean onKeyPress(Key key, KeyMask mask, KeyLocation location) {
				
				boolean doit = true;
				
				if (mask.isSet(QualifierKey.SHIFT)) {
					if (location == KeyLocation.KEYPAD) {
						doit = false;
					}
				}
				else if (config.isTabsToSpaces() && key.getCharacter() == '\t') {
					
					// System.out.println("position: " + textWidget.getCaretPosition());
					
					final Text text = editorView.getText();
					
					final long cursorPos = editorView.getCursorPosition();
					
					final String spaces = Strings.spaces(config.getTabs());
					
					final String updatedText = Strings.replaceTextRange(
							text.asString(),
							(int)cursorPos,
							0,
							spaces);

					setCurrentText(new StringText(updatedText));
					
					editorView.setCursorPosition(cursorPos + spaces.length());
					
					doit = false;
				}
				else if (key.getCharacter() == '\r') {
					
					long cursorPos = editorView.getCursorPosition();
					
					final String text = editorView.getText().asString();
				
					indentOnNewline(text, (int)cursorPos);
					
					doit = false;
				}

				return doit;
			}
		});
		
		if (listeners != null) {
			editorView.addCursorPositionListener(
			        cursorPos -> listeners.forEach(l -> l.onEditorCursorPosUpdate(cursorPos))
	        );
		}
		
		editorView.setTextModel(textModel);
	}
	
	private void updateSourceFileModel(
			TextModel textModel,
			List<EditorsListener> listeners,
			SourceParseAccess sourceParseAccess,
			SourceFileInfo sourceFile,
			DelegatingSourceFileModel delegatingSourceFileModel) {

		System.out.println("## updateSourceFileModel");
		
		parseStateMachine.tryParse(
				textModel.getText(),
				updatedModel -> {

					if (updatedModel == null) {
						throw new IllegalStateException();
					}
					
					sourceFileModel = updatedModel;

					if (listeners != null) {
						listeners.forEach(l -> l.onSourceFileModelChanged(updatedModel));
					}
					
					delegatingSourceFileModel.setDelegate(updatedModel);
					
					// Refresh text styling
					editorView.triggerStylingRefresh();
				});
		
	}
	
	@Override
	public Collection<ActionContext> getActionContexts(long cursorOffset) {
		
		// System.out.println("## get contexts for cursor " + cursorOffset);
		
		final Collection<ActionContext> actionContexts;
		
		if (sourceFileModel == null) {
			actionContexts = null;
		}
		else {
			
			final ISourceToken sourceToken = sourceFileModel.getSourceTokenAt(cursorOffset);
	
			// System.out.println("## got source token " + sourceToken);
			
			if (sourceToken != null) {
				actionContexts = new ArrayList<>();

				final ISourceTokenProperties tokenProperties = SourceFileModel.getProperties(sourceToken);
				
				actionContexts.add(new SourceTokenContext(sourceToken, tokenProperties));
			}
			else {
				actionContexts = null;
			}
		}
		
		return actionContexts;
	}
	
	@Override
	public long find(long pos, Text searchText, SearchDirection direction, SearchScope scope, boolean caseSensitive, boolean wrapSearch, boolean wholeWord) {

		final long startPos;
		
		final TextRange searchRange;
		
		switch (scope) {
		case ALL:
			
			if (pos == -1) {
				final long cursorPosition = editorView.getCursorPosition();

				startPos = direction == SearchDirection.FORWARD ? cursorPosition : cursorPosition - 1;
			}
			else {
				startPos = pos;
			}
			
			searchRange = null; // whole text
			break;
			
		case SELECTED_LINES:
			final TextRange selection = editorView.getSelection();
			
			if (selection == null) {
				// select current line
				final long cursorPos = editorView.getCursorPosition();
				
				final long lineNo = textModel.getLineAtOffset(cursorPos);
				
				startPos = textModel.getOffsetAtLine(lineNo);
				final long length = textModel.getLineLengthWithoutAnyNewline(lineNo);
				
				searchRange = new TextRange(startPos, length);
				
				editorView.select(startPos, length);
			}
			else {
				startPos = selection.getOffset();
				
				searchRange = selection;
			}
			break;
			
		default:
			throw new UnsupportedOperationException();
		}
		
		final long foundPos = textModel.find(
				searchText,
				startPos,
				searchRange,
				direction == SearchDirection.FORWARD,
				caseSensitive, wrapSearch, wholeWord);

		if (foundPos >= 0) {
			editorView.select(foundPos, searchText.length());
		}
		
		return foundPos;
	}

	@Override
	public void replace(long pos, long replaceLength, Text replacement) {

		textModel.replaceTextRange(pos, replaceLength, replacement);

		editorView.select(pos, replacement.length());
	}
	
	@Override
	public void selectAll() {
		editorView.selectAll();
	}
	
	public void setTextModel(TextModel textModel) {
		
		Objects.requireNonNull(textModel);
		
		// System.out.println("## setText: " + textModel.getText());
		
		this.textModel = textModel;

		editorView.triggerTextRefresh();
	}

	private void indentOnNewline(String text, int cursorPos) {
		int lineStart;
		
		if (cursorPos == 0) {
			lineStart = 0;
		}
		else if (text.charAt(cursorPos) == '\n') {
			lineStart = text.lastIndexOf("\n", cursorPos - 1);
		}
		else {
			lineStart = text.lastIndexOf("\n", cursorPos);
		}
		
		if (lineStart < 0) {
			lineStart = 0;
		}
		else {
			++ lineStart;
		}
		
		System.out.println("## linestart: " + lineStart + ", caretPosition " + cursorPos);
		
		final String line = text.substring(lineStart, cursorPos);
		
		
		System.out.println("### line \"" + line + "\"");
		
		final int addIndent;
		
		if (line.trim().endsWith("{")) {
			addIndent = 1;
		}
		else {
			addIndent = 0;
		}
		
		addIndentedNewline(cursorPos, line, addIndent);
	}

	private void addIndentedNewline(int caretPosition, String line, int addIndent) {
		int numWhitespace = 0;

		final int tabs = config.getTabs();
		
		for (int i = 0; i < line.length(); ++ i) {
			final char c = line.charAt(i);
			
			if (Character.isWhitespace(c)) {
				switch (c) {
				case ' ':
					++ numWhitespace;
					break;
					
				case '\t':
					numWhitespace += tabs;
					break;
					
				default:
					throw new UnsupportedOperationException("Unknown whitespace character '" + c + "'");
				}
			}
			else {
				break;
			}
		}
		
		System.out.println("Add whitespace: " + numWhitespace);
		
		final int numIndent = numWhitespace / tabs;
		
		final String newlineIndentation;
		
		if (config.isTabsToSpaces()) {
			newlineIndentation = Strings.spaces((numIndent + addIndent) * tabs);
		}
		else {
			newlineIndentation = Strings.concat("\t", numIndent + addIndent);
		}
		
		System.out.println("### add newline indentation \"" + newlineIndentation + "\"");
		
		final String newline = "\n" + newlineIndentation;
		
		final String updatedText = Strings.replaceTextRange(
				editorView.getText().asString(),
				caretPosition,
				0,
				newline);

		setCurrentText(new StringText(updatedText));
		
		editorView.setCursorPosition(caretPosition + newline.length());
	}

	public void updateText() {
		editorView.triggerTextRefresh();
	}
	
	private void setCurrentText(Text text) {
		
		Objects.requireNonNull(text);

		textModel.setText(text);
		editorView.triggerTextRefresh();
	}
}
