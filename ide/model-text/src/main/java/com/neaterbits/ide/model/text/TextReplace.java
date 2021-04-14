package com.neaterbits.ide.model.text;

import java.util.Objects;

import com.neaterbits.ide.util.ui.text.LineDelimiter;
import com.neaterbits.ide.util.ui.text.Text;

public final class TextReplace extends TextChange {

	private final Text updatedText;
	
	public TextReplace(long startPos, long replacedLength, Text changedText, Text updatedText) {
		super(startPos, replacedLength, changedText);
		
		Objects.requireNonNull(updatedText);
		
		if (updatedText.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		this.updatedText = updatedText;
	}

	@Override
	public Text getNewText() {
		return updatedText;
	}

	@Override
	public long getNewLength() {
		return updatedText.length();
	}

	@Override
	public long getChangeInNumberOfLines(LineDelimiter lineDelimiter) {
		return    updatedText.getNumberOfNewlineChars(lineDelimiter)
				- getOldText().getNumberOfNewlineChars(lineDelimiter);
	}
}

