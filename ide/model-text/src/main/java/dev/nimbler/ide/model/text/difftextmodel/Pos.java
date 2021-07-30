package dev.nimbler.ide.model.text.difftextmodel;

/**
 * Enumeration for position relative to a block of text (e.g. a line or offset)
 */
enum Pos {
    
    /**
     * Before the block of text
     */
	BEFORE,
	
	/**
	 * At the first character of block of text
	 */
	AT_START,
	
	/**
	 * Strictly within the block of text
	 */
	WITHIN,
	
	/**
	 * At the last character of the block of text
	 */
	AT_END,
	
	/**
	 * Both at start and at end (block length 1)
	 */
	AT_START_AT_END,
	
	/**
	 * After the block of text
	 */
	AFTER;
	
    /**
     * Compute the {@link Pos} enum value based on block offsets
     * 
     * @param blockOffset offset of the start of the block of text, within larger text
     * @param blockLength length of the block of text
     * @param posOffset offset to the position to relate, within larger text that block is part of
     * 
     * @return the computed {@link Pos} enum value
     */
	static Pos getPos(long blockOffset, long blockLength, long posOffset) {
		
	    if (blockOffset < 0) {
	        throw new IllegalArgumentException();
	    }
	    
	    if (blockLength <= 0) {
            throw new IllegalArgumentException();
	    }

	    if (posOffset < 0) {
            throw new IllegalArgumentException();
	    }
	    
		final Pos pos;
		
		final long endOffset = blockOffset + blockLength - 1;
		
		if (posOffset < blockOffset) {
			pos = Pos.BEFORE;
		}
        else if (posOffset == blockOffset) {
            pos = posOffset == endOffset
                    ? Pos.AT_START_AT_END
                    : Pos.AT_START;
        }
		else if (posOffset == endOffset) {
			pos = Pos.AT_END;
		}
		else if (posOffset > endOffset) {
			pos = Pos.AFTER;
		}
		else {
			pos = Pos.WITHIN;
		}
	
		return pos;
	}
}
