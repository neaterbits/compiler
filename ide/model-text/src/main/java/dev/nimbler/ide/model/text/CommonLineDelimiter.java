package dev.nimbler.ide.model.text;

import dev.nimbler.ide.util.ui.text.LineDelimiter;
import dev.nimbler.ide.util.ui.text.Text;
import dev.nimbler.ide.util.ui.text.UnixLineDelimiter;
import dev.nimbler.ide.util.ui.text.WindowsLineDelimiter;

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

    @Override
    public int getMaxLength() {
        return WindowsLineDelimiter.INSTANCE.getMaxLength();
    }

    @Override
    public boolean endsWithNewline(Text text) {
        return     UnixLineDelimiter.INSTANCE.endsWithNewline(text)
                || WindowsLineDelimiter.INSTANCE.endsWithNewline(text);
    }
}
