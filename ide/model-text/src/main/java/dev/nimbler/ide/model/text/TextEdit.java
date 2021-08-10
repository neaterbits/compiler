package dev.nimbler.ide.model.text;

import dev.nimbler.ide.util.ui.text.LineDelimiter;
import dev.nimbler.ide.util.ui.text.Text;

public abstract class TextEdit {
    
	public abstract long getChangeInNumberOfLines(LineDelimiter lineDelimiter);

	public abstract long getOldLength();

	public abstract long getNewLength();
	
	public abstract Text getOldText();
	
	public abstract Text getNewText();
	
	static long getChangeInNumberOfLines(Text text, LineDelimiter lineDelimiter) {
		return text.getNumberOfNewlineChars(lineDelimiter);
	}
}
