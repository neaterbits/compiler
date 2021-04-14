package dev.nimbler.ide.util.ui.text;

public final class TextRange {

	private final long offset;
	private final long length;

	public TextRange(long offset, long length) {
		this.offset = offset;
		this.length = length;
	}

	public long getOffset() {
		return offset;
	}

	public long getLength() {
		return length;
	}
	
	public boolean containsPos(long pos) {
		return pos >= offset && pos < offset + length;
	}

	public boolean containsPosOrEndIndex(long pos) {
		return pos >= offset && pos <= offset + length;
	}
}
