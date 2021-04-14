package dev.nimbler.ide.model.text;

import java.util.Objects;

import dev.nimbler.ide.util.ui.text.LineDelimiter;
import dev.nimbler.ide.util.ui.text.StringText;
import dev.nimbler.ide.util.ui.text.Text;
import dev.nimbler.ide.util.ui.text.TextRange;

public final class StringTextModel extends TextModel {

	private Text text;

	public StringTextModel(LineDelimiter lineDelimiter, String text) {
		this(lineDelimiter, new StringText(text));
	}

	public StringTextModel(LineDelimiter lineDelimiter, StringText text) {
		super(lineDelimiter);

		Objects.requireNonNull(text);
		
		this.text = text;
	}

	@Override
	public Text getText() {
		return text;
	}

	void setText(String text) {
		this.text = new StringText(text);
	}
	
	@Override
	public long getLength() {
		return text.length();
	}

	@Override
	public void replaceTextRange(long start, long replaceLength, Text text) {

		this.text = Text.replaceTextRange(this.text, start, replaceLength, text);
		
		/*
		System.out.println("## textmodel replace text range: \"" + text + "\"");
		System.out.println("## textModel updated text " + this.text);
		*/
	}

	@Override
	public Text getTextRange(long start, long length) {
		return text.substring(start, start + length);
	}

	@Override
	public long getOffsetAtLine(long lineIndex) {
		return text.findPosOfLineIndex(lineIndex,getLineDelimiter());
	}

	@Override
	public long getLineCount() {
		return text.getNumberOfLines(getLineDelimiter());
	}

	@Override
	public long getLineAtOffset(long offset) {
		return text.findLineIndexAtPos(offset, getLineDelimiter());
	}

	@Override
	public Text getLineWithoutAnyNewline(long lineIndex) {
		return getLine(lineIndex, false);
	}

	@Override
	public Text getLineIncludingAnyNewline(long lineIndex) {
		return getLine(lineIndex, true);
	}

	private Text getLine(long lineIndex, boolean includeAnyNewline) {
		
		final long pos = text.findPosOfLineIndex(lineIndex, getLineDelimiter());

		final Text result;
		
		if (pos < 0) {
			result = null;
		}
		else {
		
			final long eol = text.findNewline(pos, getLineDelimiter());
			
			if (eol == -1) {
				result = text.substring(pos);
			}
			else {
				
				final int lengthOfNewline = text.getNumberOfNewlineCharsForOneLineShift(eol, getLineDelimiter());
				
				result = text.substring(
						pos,
						includeAnyNewline
							? eol + lengthOfNewline
							: eol);
			}
		}
		
		return result;
	}

	@Override
	public long getCharCount() {
		return text.length();
	}

	@Override
	public long find(Text searchText, long start, TextRange range, boolean forward, boolean caseSensitive, boolean wrapSearch, boolean wholeWord) {
		return forward
					? text.findForward(searchText, start, range, caseSensitive, wrapSearch, wholeWord)
					: text.findBackward(searchText, start, range, caseSensitive, wrapSearch, wholeWord);
	}
}
