package dev.nimbler.ide.util.ui.text;

public final class UnixLineDelimiter extends LineDelimiter {

	public static final LineDelimiter INSTANCE = new UnixLineDelimiter();
	
	private UnixLineDelimiter() {
		
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
		else if (text.charAt(offset) == '\n') {
			numChars = 1;
		}
		else {
			numChars = 0;
		}
		
		return numChars;
	}

	@Override
	public String asString() {
		return "\n";
	}

    @Override
    public int getMaxLength() {
        return 1;
    }
}
