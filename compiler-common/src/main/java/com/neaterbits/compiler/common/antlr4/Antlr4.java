package com.neaterbits.compiler.common.antlr4;

import org.antlr.v4.runtime.ParserRuleContext;

import com.neaterbits.compiler.common.Context;

public class Antlr4 {

	public static Context context(ParserRuleContext antlrContext, String file) {
		return new Context(file,
				antlrContext.getStart().getLine(),
				antlrContext.getStart().getCharPositionInLine(),
				antlrContext.getStop().getLine(),
				antlrContext.getStop().getCharPositionInLine(),
				antlrContext.getText());
	}
}
