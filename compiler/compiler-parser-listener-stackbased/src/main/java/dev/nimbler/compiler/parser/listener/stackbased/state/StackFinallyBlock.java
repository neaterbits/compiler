package dev.nimbler.compiler.parser.listener.stackbased.state;

import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackFinallyBlock<STATEMENT> extends StackStatements<STATEMENT> {

	public StackFinallyBlock(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
