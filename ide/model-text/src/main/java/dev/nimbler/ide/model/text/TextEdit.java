package dev.nimbler.ide.model.text;

import dev.nimbler.ide.util.ui.text.LineDelimiter;
import dev.nimbler.ide.util.ui.text.Text;

public abstract class TextEdit {
    
	public abstract long getChangeInNumberOfLines(LineDelimiter lineDelimiter);

	public abstract long getOldLength();

	public abstract long getNewLength();
	
	public abstract Text getOldText();
	
	public abstract Text getNewText();
	
    public static TextEdit merge(TextEdit text1, long startPos1, TextEdit text2, long startPos2) {

        return TextEditMergeHelper.merge(text1, startPos1, text2, startPos2);
    }
    
    public static boolean isMergeable(TextEdit text1, long startPos1, TextEdit text2, long startPos2) {

        return TextEditMergeHelper.isMergeable(text1, startPos1, text2, startPos2);
    }

    public boolean isMergeable(long startPos1, TextEdit text2, long startPos2) {

        return TextEditMergeHelper.isMergeable(this, startPos1, text2, startPos2);
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
                + ", getNewText()=" + getNewText() + "]";
    }
}
