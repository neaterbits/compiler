package dev.nimbler.compiler.language.java.parser.antlr;

import dev.nimbler.compiler.parser.listener.stackbased.state.StackStatements;
import dev.nimbler.compiler.util.parse.ParseLogger;

final class StackJavaIfThen<STATEMENT> extends StackStatements<STATEMENT> {

	public StackJavaIfThen(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
