package com.neaterbits.compiler.antlr4;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ImmutableContext;

public class Antlr4 {

	public static Context context(ParserRuleContext antlrContext, String file, int tokenSequenceNo) {
		return new ImmutableContext(file,
				antlrContext.getStart().getLine(),
				antlrContext.getStart().getCharPositionInLine(),
				antlrContext.getStart().getStartIndex(),
				antlrContext.getStop().getLine(),
				antlrContext.getStop().getCharPositionInLine(),
				antlrContext.getStop().getStopIndex(),
				antlrContext.getText(),
				tokenSequenceNo);
	}

	public static Context context(Token token, String file, int tokenSequenceNo) {
		return new ImmutableContext(file,
				token.getLine(),
				token.getCharPositionInLine(),
				token.getStartIndex(),
				token.getLine(),
				token.getCharPositionInLine() + token.getText().length() - 1,
				token.getStopIndex(),
				token.getText(),
				tokenSequenceNo);
	}

	public static Context context(Token startToken, Token endToken, String text, String file, int tokenSequenceNo) {
		return new ImmutableContext(file,
				startToken.getLine(),
				startToken.getCharPositionInLine(),
				startToken.getStartIndex(),
				endToken.getLine(),
				endToken.getCharPositionInLine() + endToken.getText().length() - 1,
				endToken.getStopIndex(),
				text,
				tokenSequenceNo);
	}
}
