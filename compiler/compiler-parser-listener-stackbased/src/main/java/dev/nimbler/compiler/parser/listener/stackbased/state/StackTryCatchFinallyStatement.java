package dev.nimbler.compiler.parser.listener.stackbased.state;

import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackTryCatchFinallyStatement<STATEMENT, CATCH_BLOCK> extends BaseStackTryCatchFinally<STATEMENT, CATCH_BLOCK> {

	public StackTryCatchFinallyStatement(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
