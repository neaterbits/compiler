package dev.nimbler.ide.model.text;

import java.util.Objects;

import dev.nimbler.ide.util.ui.text.LineDelimiter;
import dev.nimbler.ide.util.ui.text.Text;
import dev.nimbler.ide.util.ui.text.TextRange;

/**
 * Base class for text models. These are abstractions for accessing text, e.g. for display and for update while editing.
 * 
 * This abstraction separate text edit UI from the text storage so that an implementation e.g. can support large files
 * without the UI itself requiring to store a lot of information - it is stored within the text model instead.
 */
public abstract class TextModel {

	private final LineDelimiter lineDelimiter;
	
	/**
	 * Create a text model with the specified line delimiter.
	 * 
	 * @param lineDelimiter the line delimiter
	 */
	public TextModel(LineDelimiter lineDelimiter) {
		
		Objects.requireNonNull(lineDelimiter);
		
		this.lineDelimiter = lineDelimiter;
	}

	/**
	 * Return the contents as a {@link Text}
	 * 
	 * @return retrieved {@link Text}
	 */
	public abstract Text getText();
	
	/**
	 * Set the contents of the text model.
	 * 
	 * @param text the {@link Text} to update the text model to.
	 */
	public void setText(Text text) {
		replaceTextRange(0, getLength(), text);
	}
	
	/**
	 * Replace a range of text in a text model, e.g. when editing text.
	 * 
	 * @param start index to replace at, must be greater than 0
	 * @param replaceLength the amount of text to replace, 0 or larger (0 will add text instead of replace)
	 * @param text the text to replace with (or add if replaceLength is 0)
	 */
	public abstract void replaceTextRange(long start, long replaceLength, Text text);
	
	/**
	 * Retrieve a range of text from the model.
	 * 
	 * @param start offset to the start at text to retrieve, counting from 0.
	 * @param length the length of the text range to retrieve.
	 * 
	 * @return retrieved {@link Text}
	 */
	public abstract Text getTextRange(long start, long length);

	/**
	 * Retrieve the character offset into the text model at the specified line
	 * 
	 * @param lineIndex the line index, counting from 0
	 * 
	 * @return offset, counting from 0
	 */
	public abstract long getOffsetAtLine(long lineIndex);
	
	/**
	 * Retrieve the text model's line delimiter
	 * 
	 * @return line delimiter
	 */
	
	public final LineDelimiter getLineDelimiter() {
		return lineDelimiter;
	}
	
	/**
	 * Retrieve the number of lines currently present in the text model.
	 * 
	 * @return line count
	 */
	public abstract long getLineCount();
	
	/**
	 * Compute the line at the given offset.
	 * 
	 * @param offset The offset counting from 0.
	 * 
	 * @return the line at that offset
	 */
	public abstract long getLineAtOffset(long offset);

	/**
	 * Retrieve a line at a given line index, without adding the line delimiter
	 * 
	 * @param lineIndex index of line counting from 0
	 * 
	 * @return the {@link Text} for the line, without line delimiter
	 */
	public abstract Text getLineWithoutAnyNewline(long lineIndex);

	/**
     * Retrieve a line at a given line index, adding the line delimiter
     * 
     * @param lineIndex index of line counting from 0
     * 
     * @return the {@link Text} for the line, with line delimiter
     */
	public abstract Text getLineIncludingAnyNewline(long lineIndex);

	/**
	 * Get number of characters in text model
	 * 
	 * @return
	 */
	public abstract long getCharCount();

	/**
	 * Same as {@link #getCharCount()}
	 * @return
	 */
	public abstract long getLength();
	
	public final long getLineLengthWithoutAnyNewline(long lineIndex) {
		return getLineWithoutAnyNewline(lineIndex).length();
	}

	public abstract long find(
			Text searchText,
			long startPos,
			TextRange range,
			boolean forward,
			boolean caseSensitive, boolean wrapSearch, boolean wholeWord);
}
