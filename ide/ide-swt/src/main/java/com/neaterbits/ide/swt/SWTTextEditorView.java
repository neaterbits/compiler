package com.neaterbits.ide.swt;

import java.util.Objects;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;

import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.config.TextEditorConfig;
import com.neaterbits.ide.core.ui.view.CursorPositionListener;
import com.neaterbits.ide.core.ui.view.EditorSourceActionContextProvider;
import com.neaterbits.ide.core.ui.view.KeyEventListener;
import com.neaterbits.ide.core.ui.view.TextEditorChangeListener;
import com.neaterbits.ide.core.ui.view.TextSelectionListener;
import com.neaterbits.ide.model.text.TextModel;
import com.neaterbits.ide.ui.swt.SWTViewList;
import com.neaterbits.ide.util.ui.text.StringText;
import com.neaterbits.ide.util.ui.text.TextRange;

final class SWTTextEditorView extends SWTBaseTextEditorView {

	private final Text textWidget;
	private TextDiffer textDiffer;

	private com.neaterbits.ide.util.ui.text.Text currentText;
	
	SWTTextEditorView(
			SWTViewList viewList,
			TabFolder composite,
			TextEditorConfig config,
			SourceFileResourcePath sourceFile,
			EditorSourceActionContextProvider editorSourceActionContextProvider) {

		super(composite, config, sourceFile, editorSourceActionContextProvider);

		this.textWidget = new Text(composite, SWT.MULTI|SWT.BORDER);
		
		configure(textWidget);
		
		viewList.addView(this, textWidget);
	}

	@Override
	public void setTextModel(TextModel textModel) {

		setCurrentText(textModel.getText());
		
	}

	@Override
	public void triggerTextRefresh() {
		setCurrentText(new StringText(textWidget.getText()));
	}


	private void setCurrentText(com.neaterbits.ide.util.ui.text.Text text) {
		
		Objects.requireNonNull(text);
		
		this.currentText = text;
		this.textDiffer = new TextDiffer(currentText);
		
		textWidget.setText(text.asString());
	}

	@Override
	public com.neaterbits.ide.util.ui.text.Text getText() {
		return new StringText(textWidget.getText());
	}

	@Override
	int getCursorPos() {
		return textWidget.getCaretPosition();
	}

	@Override
	void setCursorPos(int pos) {
		textWidget.setSelection(pos);
	}

	@Override
	void addKeyListener(KeyListener keyListener) {
		textWidget.addKeyListener(keyListener);
	}
	
	@Override
	public TextRange getSelection() {
		final Point selection = textWidget.getSelection();
		
		return selection != null
				? new TextRange(selection.x,  selection.y - selection.x + 1)
				: null; 
	}

	@Override
	public void addTextChangeListener(TextEditorChangeListener listener) {
		
		textWidget.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				final ReplaceTextRange replaceTextRange = textDiffer.computeReplaceTextRange(new StringText(textWidget.getText()));

				if (replaceTextRange != null) {
					listener.onTextChange(replaceTextRange.getStart(), replaceTextRange.getStart(), replaceTextRange.getText());
				}
			}
		});
	}

	@Override
	void addTextSelectionListener(TextSelectionListener textSelectionListener) {
		textWidget.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				textSelectionListener.onTextSelectionChange(hasSelectedText());
			}
		});
	}

	@Override
	public void addKeyListener(KeyEventListener listener) {
		textWidget.addKeyListener(new SWTKeyEventListener(listener));
	}

	@Override
	public void addCursorPositionListener(CursorPositionListener cursorPositionListener) {
		// throw new UnsupportedOperationException();
	}
	
	@Override
	public void selectAll() {
		textWidget.setSelection(0, textWidget.getText().length());
	}

	@Override
	public void select(long offset, long length) {
		textWidget.setSelection((int)offset, (int)length);
	}
	
	@Override
	public void triggerStylingRefresh() {
		
	}

	@Override
	boolean hasSelectedText() {
		return textWidget.getSelectionCount() != 0;
	}

	@Override
	void setFocus() {
		textWidget.setFocus();
	}

	@Override
	void setTabs(int tabs) {
		textWidget.setTabs(tabs);
	}
}
