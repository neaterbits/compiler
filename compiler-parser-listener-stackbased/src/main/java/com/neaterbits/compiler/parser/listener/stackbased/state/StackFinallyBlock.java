package com.neaterbits.compiler.parser.listener.stackbased.state;

import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackFinallyBlock<STATEMENT> extends StackStatements<STATEMENT> {

	public StackFinallyBlock(ParseLogger parseLogger) {
		super(parseLogger);
	}
}