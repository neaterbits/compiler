package com.neaterbits.ide.model.text;

import com.neaterbits.ide.util.ui.text.LineDelimiter;
import com.neaterbits.ide.util.ui.text.Text;

public class CommonLineDelimiter extends LineDelimiter {

	@Override
	public int getNumberOfNewlineCharsForOneLineShift(Text text, long offset) {

		final char c = text.charAt(offset);
		
		int newlineChars = 0;
		
		if (c == '\r') {
			newlineChars = 1;
			
			if (offset < text.length() - 1 && text.charAt(offset + 1) == '\n') {
				++ newlineChars;
			}
		}
		else if (c == '\n') {
			newlineChars = 1;
		}

		return newlineChars;
	}

	@Override
	public String asString() {
		return UnixLineDelimiter.INSTANCE.asString();
	}
}
