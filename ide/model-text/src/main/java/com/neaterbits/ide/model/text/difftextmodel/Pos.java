package com.neaterbits.ide.model.text.difftextmodel;

enum Pos {
	BEFORE,
	AT_START,
	WITHIN,
	AT_END,
	AFTER;
	
	static Pos getPos(long offset, long length, long posOffset) {
		
		final Pos pos;
		
		final long endOffset = offset + length - 1;
		
		if (posOffset < offset) {
			pos = Pos.BEFORE;
		}
		else if (posOffset == endOffset) {
			pos = Pos.AT_END;
		}
		else if (posOffset == offset) {
			pos = Pos.AT_START;
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
