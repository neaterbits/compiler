package com.neaterbits.compiler.parser.listener.stackbased.state;

import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackBlock<STATEMENT> extends StackStatements<STATEMENT> {

	public StackBlock(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
