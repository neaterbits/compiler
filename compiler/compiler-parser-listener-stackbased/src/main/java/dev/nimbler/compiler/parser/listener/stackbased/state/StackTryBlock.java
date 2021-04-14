package dev.nimbler.compiler.parser.listener.stackbased.state;

import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackTryBlock<STATEMENT> extends StackStatements<STATEMENT> {

	public StackTryBlock(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
