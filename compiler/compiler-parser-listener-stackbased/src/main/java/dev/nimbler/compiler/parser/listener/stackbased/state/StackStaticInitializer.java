package dev.nimbler.compiler.parser.listener.stackbased.state;

import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackStaticInitializer<STATEMENT> extends StackStatements<STATEMENT> {

	public StackStaticInitializer(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
