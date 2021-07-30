package dev.nimbler.ide.util.ui.text;

abstract class BaseText implements Text {

	final void checkSubstringParams(long beginIndex) {
		checkSubstringParams(this, beginIndex);
	}

	static void checkSubstringParams(Text text, long beginIndex) {

		if (beginIndex < 0) {
			throw new IllegalArgumentException();
		}
		
		if (beginIndex > text.length()) {
			throw new IllegalArgumentException();
		}
	}

	final void checkSubstringParams(long beginIndex, long endIndex) {
		checkSubstringParams(this, beginIndex, endIndex);
	}

	static void checkSubstringParams(Text text, long beginIndex, long endIndex) {
		checkSubstringParams(text, beginIndex);
		
		if (endIndex < 0) {
			throw new IllegalArgumentException();
		}
		
		if (endIndex < beginIndex) {
			throw new IllegalArgumentException();
		}
		
		if (endIndex > text.length()) {
			throw new IllegalArgumentException();
		}
	}
	
	static void checkCharAtParams(Text text, long index) {

	    if (index < 0L) {
            throw new IllegalArgumentException();
        }
        
        if (index >= text.length()) {
            throw new IllegalArgumentException();
        }
	}

	@Override
	public String toString() {
		return "\"" + asString() + "\"";
	}
	
	@Override
	public boolean equals(Object obj) {
	    
	    return equals(this, obj);
	}

    static boolean equals(Text thisText, Object obj) {

	    boolean equals;
	    
	    if (obj == null) {
	        equals = false;
	    }
	    else if (!(obj instanceof Text)) {
	        equals = false;
	    }
	    else {
	        final Text thatText = (Text)obj;
	        
	        final long length = thisText.length();
	        
	        if (length != thatText.length()) {
	            equals = false;
	        }
	        else {
	            equals = true;
	            
	            for (long i = 0; i < length; ++ i) {
	                if (thisText.charAt(i) != thatText.charAt(i)) {
	                    equals = false;
	                    break;
	                }
	            }
	        }
	    }
	    
	    return equals;
	}
}
