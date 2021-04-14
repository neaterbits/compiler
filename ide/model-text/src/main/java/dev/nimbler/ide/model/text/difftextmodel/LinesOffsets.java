package dev.nimbler.ide.model.text.difftextmodel;

import java.util.Objects;

import dev.nimbler.ide.util.ui.text.LongArray;

final class LinesOffsets {

	private final long numLines;
	private final long textLength;
	private final LongArray offsets;
	private final LongArray lineLengths;
	
	LinesOffsets(long numLines, long textLength, LongArray offsets, LongArray lineLengths) {

		Objects.requireNonNull(offsets);
		Objects.requireNonNull(lineLengths);
		
		if (offsets.getLength() != lineLengths.getLength()) {
			throw new IllegalArgumentException();
		}
		
		if (numLines > offsets.getLength()) {
			throw new IllegalArgumentException();
		}
		
		this.numLines = numLines;
		this.textLength = textLength;
		this.offsets = offsets;
		this.lineLengths = lineLengths;
	}

	long getNumLines() {
		return numLines;
	}
	
	long getTextLength() {
		return textLength;
	}

	long getOffsetForLine(long lineIndex) {
		
		if (lineIndex < 0) {
			throw new IllegalArgumentException();
		}
		
		if (lineIndex >= numLines) {
			throw new IllegalArgumentException();
		}
		
		return offsets.get(lineIndex);
	}
	
	long getLineAtOffset(long offset) {
		
		if (offset < 0) {
			throw new IllegalArgumentException();
		}
		
		if (offset >= textLength) {
			throw new IllegalArgumentException();
		}
		
		final long index = offsets.binarySearchForPrevious(offset);
		
		return index;
	}

	long getLengthOfLineWithAnyNewline(long lineIndex) {

		if (lineIndex < 0) {
			throw new IllegalArgumentException();
		}
		
		if (lineIndex >= numLines) {
			throw new IllegalArgumentException();
		}

		return lineLengths.get(lineIndex);
	}
}
