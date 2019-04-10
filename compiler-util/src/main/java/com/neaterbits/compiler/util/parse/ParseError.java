package com.neaterbits.compiler.util.parse;

public abstract class ParseError extends CompileError {

    private final int line;
    private final int charPositionInLine;
    private final String msg;
	
    public ParseError(int line, int charPositionInLine, String msg) {
		this.line = line;
		this.charPositionInLine = charPositionInLine;
		this.msg = msg;
	}

	public final int getLine() {
		return line;
	}

	public final int getCharPositionInLine() {
		return charPositionInLine;
	}

	@Override
	public final String getMessage() {
		return msg;
	}
}
