package dev.nimbler.ide.model.text;

import dev.nimbler.ide.util.ui.text.LineDelimiter;
import dev.nimbler.ide.util.ui.text.Text;

public final class WindowsLineDelimiter extends LineDelimiter {

	public static final LineDelimiter INSTANCE = new WindowsLineDelimiter();
	
	private WindowsLineDelimiter() {
		
	}
	
	@Override
	public int getNumberOfNewlineCharsForOneLineShift(Text text, long offset) {

		final int numChars;
		
		if (offset < 0) {
			throw new IllegalArgumentException();
		}
		else if (offset >= text.length()) {
			throw new IllegalArgumentException();
		}
		else if (text.charAt(offset) == '\r') {
			
			if (offset + 1 >= text.length()) {
				throw new IllegalStateException();
			}
			
			if (text.charAt(offset + 1) == '\n') {
				numChars = 2;
			}
			else {
				throw new IllegalStateException();
			}
		}
		else {
			numChars = 0;
		}
		
		return numChars;
	}

	@Override
	public String asString() {
		return "\r\n";
	}
}
