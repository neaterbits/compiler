package com.neaterbits.compiler.util.parse.listener.stackbased.state.base;

import com.neaterbits.compiler.util.parse.ParseLogger;

public abstract class StackEntry {

	final ParseLogger parseLogger;

	protected StackEntry(ParseLogger parseLogger) {
		this.parseLogger = parseLogger;
	}

	protected final ParseLogger getParseLogger() {
		return parseLogger;
	}
}
