package com.neaterbits.compiler.common.antlr4;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

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

	public static Context context(Token token, String file) {
		return new Context(file,
				token.getLine(),
				token.getCharPositionInLine(),
				token.getLine(),
				token.getCharPositionInLine() + token.getText().length() - 1,
				token.getText());
	}
}
