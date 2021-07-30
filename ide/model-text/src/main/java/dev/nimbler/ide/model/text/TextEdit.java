package dev.nimbler.ide.model.text;

import dev.nimbler.ide.util.ui.text.LineDelimiter;
import dev.nimbler.ide.util.ui.text.Text;

public abstract class TextEdit {

	private final long startPos;

	public abstract long getChangeInNumberOfLines(LineDelimiter lineDelimiter);

	public abstract long getOldLength();

	public abstract long getNewLength();
	
	public abstract Text getOldText();
	
	public abstract Text getNewText();
	
	public final TextEdit merge(TextEdit other) {
		
	    // Must overlap
		if (startPos + getNewLength() < other.startPos) {
			throw new IllegalArgumentException();
		}
		
		final long totalOldLength = getOldLength() + other.getOldLength();
		final long totalNewLength = getNewLength() + other.getNewLength();
		
		final TextEdit textEdit;
		
		if (totalOldLength == 0) {
			if (totalNewLength == 0) {
				textEdit = null;
			}
			else {
				textEdit = new TextAdd(startPos, getNewText().merge(other.getNewText()));
			}
		}
		else {
			if (totalNewLength == 0) {
				textEdit = new TextRemove(startPos, totalOldLength, getOldText().merge(other.getOldText()));
			}
			else {
				textEdit = new TextReplace(
						startPos,
						totalOldLength,
						getOldText().merge(other.getOldText()),
						getNewText().merge(other.getNewText()));
						
			}
		}
		
		return textEdit;
	}
	
    public static TextEdit merge(TextEdit text1, long startPos1, TextEdit text2, long startPos2) {

        return TextEditMerge.merge(text1, startPos1, text2, startPos2);
    }

	public TextEdit(long startPos) {
		this.startPos = startPos;
	}

	public final long getStartPos() {
		return startPos;
	}

	static long getChangeInNumberOfLines(Text text, LineDelimiter lineDelimiter) {
		return text.getNumberOfNewlineChars(lineDelimiter);
	}

    @Override
    public String toString() {

        return getClass().getSimpleName()
                
                + " [getOldLength()=" + getOldLength()
                + ", getNewLength()=" + getNewLength()
                + ", getOldText()=" + getOldText()
                + ", getNewText()=" + getNewText()
                + ", getStartPos()=" + getStartPos() + "]";
    }
}
