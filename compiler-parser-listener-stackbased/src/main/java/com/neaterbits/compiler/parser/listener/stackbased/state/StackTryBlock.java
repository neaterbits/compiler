package com.neaterbits.compiler.parser.listener.stackbased.state;

import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackTryBlock<STATEMENT> extends StackStatements<STATEMENT> {

	public StackTryBlock(ParseLogger parseLogger) {
		super(parseLogger);
	}
}