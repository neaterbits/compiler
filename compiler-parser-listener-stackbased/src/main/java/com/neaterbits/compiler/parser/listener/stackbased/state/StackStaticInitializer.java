package com.neaterbits.compiler.parser.listener.stackbased.state;

import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackStaticInitializer<STATEMENT> extends StackStatements<STATEMENT> {

	public StackStaticInitializer(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
