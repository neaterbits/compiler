package com.neaterbits.compiler.util;

public final class TokenSequenceNoGenerator {

	private int tokenSequenceNo;
	
	public int getNextTokenSequenceNo() {
		return tokenSequenceNo ++;
	}
}
