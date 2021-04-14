package dev.nimbler.compiler.parser.listener.stackbased.state;

import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackBlock<STATEMENT> extends StackStatements<STATEMENT> {

	public StackBlock(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
