package dev.nimbler.compiler.antlr4;

public interface ParserContext extends ParserPosition {

	String getRuleName();
	
	String getText();
}
