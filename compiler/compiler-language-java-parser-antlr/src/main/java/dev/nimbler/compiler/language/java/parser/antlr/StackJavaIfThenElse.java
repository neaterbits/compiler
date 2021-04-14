package dev.nimbler.compiler.language.java.parser.antlr;

import dev.nimbler.compiler.parser.listener.stackbased.state.StackStatements;
import dev.nimbler.compiler.util.parse.ParseLogger;

final class StackJavaIfThenElse<STATEMENT> extends StackStatements<STATEMENT> {

	public StackJavaIfThenElse(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
