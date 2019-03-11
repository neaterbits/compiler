package com.neaterbits.compiler.antlr4;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import com.neaterbits.compiler.util.Context;

public class Antlr4 {

	public static Context context(ParserRuleContext antlrContext, String file) {
		return new Context(file,
				antlrContext.getStart().getLine(),
				antlrContext.getStart().getCharPositionInLine(),
				antlrContext.getStart().getStartIndex(),
				antlrContext.getStop().getLine(),
				antlrContext.getStop().getCharPositionInLine(),
				antlrContext.getStop().getStopIndex(),
				antlrContext.getText());
	}

	public static Context context(Token token, String file) {
		return new Context(file,
				token.getLine(),
				token.getCharPositionInLine(),
				token.getStartIndex(),
				token.getLine(),
				token.getCharPositionInLine() + token.getText().length() - 1,
				token.getStopIndex(),
				token.getText());
	}
}