package dev.nimbler.ide.util.ui.text;

import java.util.Objects;

public final class StringText extends BaseText implements Text {

	private final String string;

	public StringText(String string) {
	
		Objects.requireNonNull(string);
		
		this.string = string;
	}

	private int checkIndex(long index) {
		if (index > Integer.MAX_VALUE) {
			throw new IllegalArgumentException();
		}
		
		return (int)index;
	}
	
	@Override
	public boolean isEmpty() {
		return string.isEmpty();
	}

	@Override
	public long length() {
		return string.length();
	}

	@Override
	public char charAt(long index) {
		return string.charAt(checkIndex(index));
	}

	@Override
	public Text merge(Text other) {
		return new StringText(asString() + other.asString());
	}

	@Override
    public Text merge(String other) {
	    
	    Objects.requireNonNull(other);
	    
        return new StringText(asString() + other);
    }

    @Override
	public Text substring(long beginIndex) {
		checkSubstringParams(beginIndex);

		return new StringText(string.substring(checkIndex(beginIndex)));
	}

	@Override
	public Text substring(long beginIndex, long endIndex) {

		checkSubstringParams(beginIndex, endIndex);
		
		return new StringText(string.substring(checkIndex(beginIndex), checkIndex(endIndex)));
	}

	@Override
	public String asString() {
		return string;
	}
}
