package dev.nimbler.ide.swt;

import dev.nimbler.ide.util.ui.text.Text;

final class ReplaceText {
	private final long start;
	private final long length;
	private final Text removedText;
	private final Text addedText;
	
	ReplaceText(long start, long length, Text removedText, Text addedText) {
		this.start = start;
		this.length = length;
		this.removedText = removedText;
		this.addedText = addedText;
	}

	long getStart() {
		return start;
	}

	long getLength() {
		return length;
	}

	Text getRemovedText() {
		return removedText;
	}

	Text getAddedText() {
		return addedText;
	}
}
