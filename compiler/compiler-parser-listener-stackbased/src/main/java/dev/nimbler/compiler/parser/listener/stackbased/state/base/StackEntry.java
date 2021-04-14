package dev.nimbler.compiler.parser.listener.stackbased.state.base;

import dev.nimbler.compiler.util.parse.ParseLogger;

public abstract class StackEntry {

	final ParseLogger parseLogger;

	protected StackEntry(ParseLogger parseLogger) {
		this.parseLogger = parseLogger;
	}

	protected final ParseLogger getParseLogger() {
		return parseLogger;
	}
}
