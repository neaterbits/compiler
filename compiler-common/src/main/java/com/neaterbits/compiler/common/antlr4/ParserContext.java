package com.neaterbits.compiler.common.antlr4;

public interface ParserContext extends ParserPosition {

	String getRuleName();
	
	String getText();
}
