package com.neaterbits.compiler.common.antlr4;

public interface ParserPosition {
	int getStartLine();

	int getStartColumn();

	int getEndLine();

	int getEndColumn();

}
