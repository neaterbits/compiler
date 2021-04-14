package dev.nimbler.ide.swt;

import dev.nimbler.ide.util.ui.text.Text;

class ReplaceTextRange {

	private final long start;
	private final long length;
	private final Text text;

	ReplaceTextRange(long start, long length, Text text) {
		this.start = start;
		this.length = length;
		this.text = text;
	}

	long getStart() {
		return start;
	}
	
	long getLength() {
		return length;
	}
	
	Text getText() {
		return text;
	}

	@Override
	public String toString() {
		return "ReplaceTextRange [start=" + start + ", length=" + length + ", text=" + text + "]";
	}
}
