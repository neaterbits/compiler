package com.neaterbits.ide.model.text;

import java.util.Objects;

import com.neaterbits.ide.util.ui.text.LineDelimiter;
import com.neaterbits.ide.util.ui.text.Text;
import com.neaterbits.ide.util.ui.text.TextRange;

public abstract class TextModel {

	private final LineDelimiter lineDelimiter;
	
	public TextModel(LineDelimiter lineDelimiter) {
		
		Objects.requireNonNull(lineDelimiter);
		
		this.lineDelimiter = lineDelimiter;
	}

	public abstract Text getText();
	
	public void setText(Text text) {
		replaceTextRange(0, getLength(), text);
	}
	
	public abstract void replaceTextRange(long start, long replaceLength, Text text);
	
	public abstract Text getTextRange(long start, long length);

	public abstract long getOffsetAtLine(long lineIndex);
	
	public final LineDelimiter getLineDelimiter() {
		return lineDelimiter;
	}
	
	public abstract long getLineCount();
	
	public abstract long getLineAtOffset(long offset);

	public abstract Text getLineWithoutAnyNewline(long lineIndex);
	public abstract Text getLineIncludingAnyNewline(long lineIndex);

	public abstract long getCharCount();

	public abstract long getLength();
	
	public final long getLineLengthWithoutAnyNewline(long lineIndex) {
		return getLineWithoutAnyNewline(lineIndex).length();
	}

	public abstract long find(
			Text searchText,
			long startPos,
			TextRange range,
			boolean forward,
			boolean caseSensitive, boolean wrapSearch, boolean wholeWord);
}
