package dev.nimbler.ide.util.ui.text;

public abstract class LineDelimiter {

	public abstract int getNumberOfNewlineCharsForOneLineShift(Text text, long offset);
	
	public abstract String asString();
	
	public abstract int getMaxLength();
	
	public boolean endsWithNewline(Text text) {
        return text.length() >= getMaxLength() && text.endsWith(asString());
    }	
	
	public static LineDelimiter getSystemLineDelimiter() {
	    
	    final LineDelimiter lineDelimiter;
	    
	    switch (System.lineSeparator()) {
	    case "\n":
	        lineDelimiter = UnixLineDelimiter.INSTANCE;
	        break;
	        
	    case "\r\n":
	        lineDelimiter = WindowsLineDelimiter.INSTANCE;
	        break;
	        
	    default:
	        throw new IllegalStateException();
	    }
	    
	    return lineDelimiter;
	}
}
