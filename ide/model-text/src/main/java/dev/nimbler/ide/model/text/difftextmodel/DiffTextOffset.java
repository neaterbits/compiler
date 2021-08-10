package dev.nimbler.ide.model.text.difftextmodel;

import java.util.Objects;

import org.jutils.Value;

import dev.nimbler.ide.model.text.PosEdit;
import dev.nimbler.ide.model.text.TextEdit;
import dev.nimbler.ide.util.ui.text.LineDelimiter;
import dev.nimbler.ide.util.ui.text.Text;

/**
 * Stores a {@link PosEdit} with the current distance to the next text edit (in number of characters, including line delimiter).
 * 
 */
final class DiffTextOffset {

    // text edit that caused the diff
	private final PosEdit edit;
	
	// distance in characters (including line delimiter) to the next edit
	private final long distanceToNextTextEdit;
	
	DiffTextOffset(PosEdit edit, long distanceToNextTextEdit) {

		Objects.requireNonNull(edit);
		
		if (distanceToNextTextEdit <= 0L) {
		    throw new IllegalArgumentException();
		}
		
		this.edit = edit;
		this.distanceToNextTextEdit = distanceToNextTextEdit;
	}

	// offset as seen from latest edit
	
	/**
	 * Retrieves the TextEdit that caused the diff to be added to the {@link DiffTextModel}.
	 * 
	 * @return a {@link TextEdit}
	 */
	PosEdit getEdit() {
		return edit;
	}

	/**
	 * Return distance to next text edit in number of characters - including line delimiter.
	 * 
	 * @return distance to next edit
	 */
	long getDistanceToNextTextEdit() {
		return distanceToNextTextEdit;
	}

	long getNewLength() {
		return edit.getNewLength();
	}
	
	Text getText() {
		return edit.getNewText();
	}
	
	/**
	 * Retrieve a line from the text edit, indexed from the beginning of the line text.
	 * 
	 * @param lineIndex offset of line, counting from 0
	 * @param lineDelimiter the line delimiter
	 * 
	 * @return a text object for the line
	 */
	Text getLine(long lineIndex, LineDelimiter lineDelimiter) {
		
		System.out.println("## getLine " + lineIndex + " from \"" + edit.getNewText().asString() + "\"");
		
		final Text text = edit.getNewText();
		
		long lineOffset = 0L;
		
		for (long line = 0; line < lineIndex; ++ line) {
			lineOffset = text.findBeginningOfNextLine(lineOffset, lineDelimiter);
		}
		
		final Text result;
		
		if (lineOffset < 0) {
			result = null;
		}
		else {
		
			final long nextLineOffset = text.findBeginningOfNextLine(lineOffset, lineDelimiter);
			
			result = text.substring(
					lineOffset,
					nextLineOffset < 0
						? text.length()
						: nextLineOffset);
		}
		
		return result;
	}

	long getOffsetForLine(long lineIndex, LineDelimiter lineDelimiter) {
		
		final Text text = edit.getNewText();
		
		long lineOffset = 0L;
		
		for (long line = 0; line < lineIndex; ++ line) {
			lineOffset = text.findBeginningOfNextLine(lineOffset, lineDelimiter);
		}

		return lineOffset;
	}
	
	long getLineAtOffset(long offset, LineDelimiter lineDelimiter) {

		final Text text = edit.getNewText();

		return text.findLineIndexAtPos(offset, lineDelimiter);
	}
	
	long getChangeInNumberOfLines(LineDelimiter lineDelimiter) {
		return edit.getChangeInNumberOfLines(lineDelimiter);
	}
	
	long getLastLineInChunk(long startLineInChunk, LineDelimiter lineDelimiter) {

		final Value<Long> value = new Value<>();
		
		final Text text = getText();
		
		final long numNewlineChars = text.getNumberOfNewlineChars(lineDelimiter, value);

		final long toAdd;
		
		System.out.println("## offsetOfNext=" + value.get() + ", length=" + text.length());
		
		if (numNewlineChars == 0) {
			toAdd = 0;
		}
		else if (value.get() == text.length()) {
			// text ends at newline
			toAdd = numNewlineChars - 1;
		}
		else if (value.get() > text.length()) {
			throw new IllegalStateException();
		}
		else {
			toAdd = numNewlineChars;
		}
		
		return startLineInChunk + toAdd;
	}

	@Override
	public String toString() {
		return "DiffTextOffset [textEdit=" + edit + ", distanceToNextTextEdit=" + distanceToNextTextEdit + "]";
	}
}
