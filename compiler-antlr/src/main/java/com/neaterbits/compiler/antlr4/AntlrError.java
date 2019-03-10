package com.neaterbits.compiler.antlr4;

import org.antlr.v4.runtime.RecognitionException;

import com.neaterbits.compiler.util.parse.ParseError;

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
