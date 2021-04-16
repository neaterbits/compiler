package dev.nimbler.ide.util.ui.text;

public class CharText extends CharArray64Bit implements Text, TextBuilder  {

	private boolean toTextCalled;
	
	private static final int MAX_SUBARRAY_SIZE = 1 << 12;
	
	public CharText() {
		super(1, 100, MAX_SUBARRAY_SIZE);
	}

	public CharText(String string) {
		this(string.length());
	
		append(string);
	}
	
	public CharText(long length) {
		super(
			getArraysNumEntries(length, MAX_SUBARRAY_SIZE),
			(int)Math.min(length, MAX_SUBARRAY_SIZE),
			MAX_SUBARRAY_SIZE);
	}

	private CharText(CharText toCopy) {
		super(toCopy);
	}
	
	@Override
	public boolean isEmpty() {
		return getLength() == 0L;
	}

	@Override
	public long length() {
		return getLength();
	}

	@Override
	public char charAt(long index) {
		return arrays[getArraysIndex(index)][getSubArrayIndex(index)];
	}

	@Override
	public Text merge(Text other) {
		
		final CharText result = new CharText(this);
		
		result.append(other);
		
		return result;
	}

	@Override
    public Text merge(String other) {

        final CharText result = new CharText(this);
        
        result.append(other);
        
        return result;
    }

    @Override
	public Text substring(long beginIndex) {
		
		BaseText.checkSubstringParams(this, beginIndex);
		
		return new CharTextSubstring(this, beginIndex, length());
	}

	@Override
	public Text substring(long beginIndex, long endIndex) {
		
		BaseText.checkSubstringParams(this, beginIndex, endIndex);
		
		return new CharTextSubstring(this, beginIndex, endIndex);
	}
	
	@Override
	public String asString() {
		
		if (getLength() > Integer.MAX_VALUE) {
			throw new IllegalStateException();
		}
		
		final int length = (int)getLength();
		
		final StringBuilder sb = new StringBuilder(length);
		
		final int numArrays = getArraysNumEntries(length);
		
		for (int i = 0; i < numArrays; ++ i) {
			
			if (i < numArrays - 1) {
				sb.append(arrays[i], 0, maxArraySize);
			}
			else {
				final int lastArrayLength = getSubArrayIndex(length);
				
				sb.append(arrays[i], 0, lastArrayLength);
			}
		}
		
		return sb.toString();
	}
	
	

	// TextBuilder

	private void checkMutable() {
		
		if (toTextCalled) {
			throw new IllegalStateException();
		}
	}

	@Override
	public TextBuilder append(Text text) {
		
		checkMutable();
		
		for (long i = 0; i < text.length(); ++ i) {
			appendChar(text.charAt(i));
		}
		
		return this;
	}

	@Override
	public TextBuilder append(String string) {

		checkMutable();

		for (int i = 0; i < string.length(); ++ i) {
			appendChar(string.charAt(i));
		}
		
		return this;
	}

	@Override
	public TextBuilder append(char c) {

		checkMutable();

		appendChar(c);
		
		return this;
	}

	private void appendChar(char c) {
		
		
		final long length = getLength();
		
		final long indices = prepareArraysForSet(length);
		
		arrays[(int)(indices >>> 32)][(int)(indices & 0xFFFFFFFFL)] = c;
	}

	@Override
	public Text toText() {
		return this;
	}

	@Override
	public String toString() {
		return "\"" + asString() + "\"";
	}
}
