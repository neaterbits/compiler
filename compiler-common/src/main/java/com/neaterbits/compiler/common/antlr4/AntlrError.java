package com.neaterbits.compiler.common.antlr4;

import org.antlr.v4.runtime.RecognitionException;

import com.neaterbits.compiler.common.ParseError;

public final class AntlrError extends ParseError {
    private final RecognitionException e;

    AntlrError(int line, int charPositionInLine, String msg, RecognitionException e) {

    	super(line, charPositionInLine, msg);
    	
    	this.e = e;
	}

	public RecognitionException getException() {
		return e;
	}
}
