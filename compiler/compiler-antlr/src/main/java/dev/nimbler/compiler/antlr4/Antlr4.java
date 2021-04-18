package dev.nimbler.compiler.antlr4;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.jutils.parse.context.Context;
import org.jutils.parse.context.FullContext;
import org.jutils.parse.context.ImmutableFullContext;

public class Antlr4 {

	public static FullContext context(ParserRuleContext antlrContext, String file) {
		return new ImmutableFullContext(file,
				antlrContext.getStart().getLine(),
				antlrContext.getStart().getCharPositionInLine(),
				antlrContext.getStart().getStartIndex(),
				antlrContext.getStop().getLine(),
				antlrContext.getStop().getCharPositionInLine(),
				antlrContext.getStop().getStopIndex(),
				antlrContext.getText());
	}

	public static FullContext context(Token token, String file) {
		return new ImmutableFullContext(file,
				token.getLine(),
				token.getCharPositionInLine(),
				token.getStartIndex(),
				token.getLine(),
				token.getCharPositionInLine() + token.getText().length() - 1,
				token.getStopIndex(),
				token.getText());
	}

	public static Context context(Token startToken, Token endToken, String text, String file) {
		return new ImmutableFullContext(file,
				startToken.getLine(),
				startToken.getCharPositionInLine(),
				startToken.getStartIndex(),
				endToken.getLine(),
				endToken.getCharPositionInLine() + endToken.getText().length() - 1,
				endToken.getStopIndex(),
				text);
	}
}
