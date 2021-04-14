package com.neaterbits.compiler.antlr4;

public interface ParserPosition {
	int getStartLine();

	int getStartColumn();

	int getEndLine();

	int getEndColumn();

}
