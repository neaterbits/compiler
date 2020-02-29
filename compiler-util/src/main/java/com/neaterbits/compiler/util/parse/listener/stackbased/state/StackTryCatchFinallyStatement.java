package com.neaterbits.compiler.util.parse.listener.stackbased.state;

import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackTryCatchFinallyStatement<STATEMENT, CATCH_BLOCK> extends BaseStackTryCatchFinally<STATEMENT, CATCH_BLOCK> {

	public StackTryCatchFinallyStatement(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
