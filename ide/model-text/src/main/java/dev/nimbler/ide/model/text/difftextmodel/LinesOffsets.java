package dev.nimbler.ide.model.text.difftextmodel;

import java.util.Objects;

import dev.nimbler.ide.util.ui.text.LongArray;

/**
 * Immutable storage of offsets to lines and their lengths. Also keeps number of lines and the number of characters in the text.
 * Does not keep the text itself.
 * 
 */

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

	/**
	 * Get the offset of the beginning of a line
	 * 
	 * @param lineIndex line number, counting from 0
	 * 
	 * @return offset of start of line, counting from 0
	 */
	long getOffsetForLine(long lineIndex) {
		
		if (lineIndex < 0) {
			throw new IllegalArgumentException();
		}
		
		if (lineIndex >= numLines) {
			throw new IllegalArgumentException();
		}
		
		return offsets.get(lineIndex);
	}
	
	/**
	 * Get the line offset of the line containing the character at given offset
	 * 
	 * @param offset character offset, counting from 0
	 * 
	 * @return line index, counting from 0
	 */
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

	/**
	 * Get length of line including newline at given index
	 * 
	 * @param lineIndex index of line, counting from 0
	 * 
	 * @return offset into text, counting from 0
	 */
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
