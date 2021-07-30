package dev.nimbler.ide.util.ui.text;

import java.util.Objects;

final class CharTextSubstring extends BaseText implements Text {

	private final CharText charText;
	private final long beginIndex;
	private final long endIndex;
	
	CharTextSubstring(CharText charText, long beginIndex, long endIndex) {

		Objects.requireNonNull(charText);
		
		this.charText = charText;
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
	}

	@Override
	public boolean isEmpty() {
		return length() == 0L;
	}

	@Override
	public long length() {
		return endIndex - beginIndex;
	}

	@Override
	public char charAt(long index) {
		
	    BaseText.checkCharAtParams(this, index);
		
		return charText.charAt(beginIndex + index);
	}

	@Override
	public Text merge(Text other) {
		
		final CharText charText = new CharText(length() + other.length());
		
		charText.append(this);
		charText.append(other);
		
		return charText;
	}

	@Override
    public Text merge(String other) {

	    final CharText charText = new CharText(length() + other.length());
        
        charText.append(this);
        charText.append(other);
        
        return charText;
    }

    @Override
	public Text substring(long beginIndex) {

		checkSubstringParams(beginIndex);
		
		return charText.substring(this.beginIndex + beginIndex, this.endIndex);
	}

	@Override
	public Text substring(long beginIndex, long endIndex) {

		checkSubstringParams(beginIndex, endIndex);

		return charText.substring(this.beginIndex + beginIndex, this.beginIndex + endIndex);
	}

	@Override
	public String asString() {

		if (length() > Integer.MAX_VALUE) {
			throw new IllegalStateException();
		}
		
		final StringBuilder sb = new StringBuilder((int)length());
		
		for (long i = beginIndex; i < endIndex; ++ i) {
			sb.append(charText.charAt(i));
		}
		
		return sb.toString();
	}
}
