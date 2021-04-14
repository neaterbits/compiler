package dev.nimbler.compiler.antlr4;

public interface ParserPosition {
	int getStartLine();

	int getStartColumn();

	int getEndLine();

	int getEndColumn();

}
