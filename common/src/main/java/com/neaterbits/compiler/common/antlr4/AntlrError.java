package com.neaterbits.compiler.common.antlr4;

import org.antlr.v4.runtime.RecognitionException;

public final class AntlrError {
    private final int line;
    private final int charPositionInLine;
    private final String msg;
    private final RecognitionException e;

    AntlrError(int line, int charPositionInLine, String msg, RecognitionException e) {
		this.line = line;
		this.charPositionInLine = charPositionInLine;
		this.msg = msg;
		this.e = e;
	}

	public int getLine() {
		return line;
	}

	public int getCharPositionInLine() {
		return charPositionInLine;
	}

	public String getMsg() {
		return msg;
	}

	public RecognitionException getException() {
		return e;
	}
}
