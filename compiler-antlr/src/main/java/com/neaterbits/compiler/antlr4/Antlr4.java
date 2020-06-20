package com.neaterbits.compiler.antlr4;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.FullContext;
import com.neaterbits.compiler.util.ImmutableContext;

public class Antlr4 {

	public static FullContext context(ParserRuleContext antlrContext, String file) {
		return new ImmutableContext(file,
				antlrContext.getStart().getLine(),
				antlrContext.getStart().getCharPositionInLine(),
				antlrContext.getStart().getStartIndex(),
				antlrContext.getStop().getLine(),
				antlrContext.getStop().getCharPositionInLine(),
				antlrContext.getStop().getStopIndex(),
				antlrContext.getText());
	}

	public static FullContext context(Token token, String file) {
		return new ImmutableContext(file,
				token.getLine(),
				token.getCharPositionInLine(),
				token.getStartIndex(),
				token.getLine(),
				token.getCharPositionInLine() + token.getText().length() - 1,
				token.getStopIndex(),
				token.getText());
	}

	public static Context context(Token startToken, Token endToken, String text, String file) {
		return new ImmutableContext(file,
				startToken.getLine(),
				startToken.getCharPositionInLine(),
				startToken.getStartIndex(),
				endToken.getLine(),
				endToken.getCharPositionInLine() + endToken.getText().length() - 1,
				endToken.getStopIndex(),
				text);
	}
}
