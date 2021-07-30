package dev.nimbler.ide.model.text.difftextmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.nimbler.ide.model.text.TextAdd;
import dev.nimbler.ide.model.text.TextEdit;
import dev.nimbler.ide.model.text.TextModel;
import dev.nimbler.ide.model.text.TextRemove;
import dev.nimbler.ide.model.text.TextReplace;
import dev.nimbler.ide.util.ui.text.LineDelimiter;
import dev.nimbler.ide.util.ui.text.Text;
import dev.nimbler.ide.util.ui.text.TextRange;

/**
 * A text model based on maintaining the diffs from an initial text.
 * This allows for large file edits where the e.g. the initial text can be an mmap()ed file
 * so that we do not have to keep the file in-memory, only the edit diffs are kept in-memory.
 */
public final class DiffTextModel extends TextModel {

    // all edits in chronological order
	private final List<TextEdit> edits;
	
	// the initial text we are storing diffs from
	private final Text initialText;

	// offsets to all lines and their length in the initial text
	private LinesOffsets initialOffsets;
	
	// text edits sorted by where they appear in the text
	private final DiffTextOffsets diffOffsets;
	
	// current length of the edited text
	private long curTextLength;
	
	public DiffTextModel(LineDelimiter lineDelimiter, Text text) {
		
		super(lineDelimiter);
		
		Objects.requireNonNull(text);

		this.initialText = text;
		
		this.initialOffsets = LinesFinder.findLineOffsets(text, lineDelimiter);
		
		this.edits = new ArrayList<>();
		
		this.diffOffsets = new DiffTextOffsets();
		
		this.curTextLength = initialText.length();
	}
	

	@Override
	public Text getText() {

		return diffOffsets.getText(curTextLength, initialText, initialOffsets);
	}
	
	@Override
	public long getLength() {
		return curTextLength;
	}

	@Override
	public void replaceTextRange(long startPos, long replaceLength, Text text) {

		final TextEdit textEdit;
		
		if (replaceLength == 0) {
			if (text.isEmpty()) {
				throw new IllegalArgumentException();
			}
			else {
				textEdit = new TextAdd(startPos, text);
			}
		}
		else {

			final Text changedText = getTextRange(startPos, replaceLength);
			
			if (text.isEmpty()) {
				textEdit = new TextRemove(startPos, replaceLength, changedText);
			}
			else {
				textEdit = new TextReplace(startPos, replaceLength, changedText, text);
			}
		}
		
		edits.add(textEdit);
		
		curTextLength -= replaceLength;
		curTextLength += text.length();
		
		diffOffsets.applyTextEdit(textEdit, initialOffsets);
	}

	@Override
	public Text getTextRange(long start, long length) {
		
		if (start < 0) {
			throw new IllegalArgumentException();
		}
		
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		
		if (start + length > curTextLength) {
			throw new IllegalArgumentException();
		}
		
		return diffOffsets.getTextRange(start, length, curTextLength, initialText, initialOffsets);
	}

	@Override
	public long getOffsetAtLine(long lineIndex) {
		
		return diffOffsets.getOffsetForLine(lineIndex, curTextLength, getLineDelimiter(), initialOffsets);
	}

	@Override
	public long getLineCount() {
		
		return diffOffsets.getLineCount(curTextLength, getLineDelimiter(), initialOffsets);
	}
	
	@Override
	public long getLineAtOffset(long offset) {
		
		if (offset >= curTextLength) {
			throw new IllegalArgumentException();
		}
		
		return diffOffsets.getLineAtOffset(offset, curTextLength, getLineDelimiter(), initialOffsets);
	}

	@Override
	public Text getLineWithoutAnyNewline(long lineIndex) {

		final Text including = getLineIncludingAnyNewline(lineIndex);

		final Text result;
		
		if (including != null) {
			
			System.out.println("## including: " + including.toString() + " for " + lineIndex);
			
			final long newlinePos = including.findNewline(0, getLineDelimiter());
			
			result = newlinePos < 0 ? including : including.substring(0, newlinePos);
		}
		else {
			result = null;
		}
		
		return result;
	}

	@Override
	public Text getLineIncludingAnyNewline(long lineIndex) {
		return diffOffsets.getLine(lineIndex, curTextLength, getLineDelimiter(), initialText, initialOffsets);
	}

	@Override
	public long getCharCount() {
		return diffOffsets.getCharCount(curTextLength, initialOffsets);
	}


	@Override
	public long find(Text searchText, long start, TextRange range, boolean forward, boolean caseSensitive, boolean wrapSearch, boolean wholeWord) {
		throw new UnsupportedOperationException();
	}
}
