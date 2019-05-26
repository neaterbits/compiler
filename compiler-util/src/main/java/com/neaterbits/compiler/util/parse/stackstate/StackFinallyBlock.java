package com.neaterbits.compiler.util.parse.stackstate;

import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackFinallyBlock<STATEMENT> extends StackStatements<STATEMENT> {

	public StackFinallyBlock(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
