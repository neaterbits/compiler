package dev.nimbler.ide.util.ui.text;

public abstract class LineDelimiter {

	public abstract int getNumberOfNewlineCharsForOneLineShift(Text text, long offset);
	
	public abstract String asString();
}
