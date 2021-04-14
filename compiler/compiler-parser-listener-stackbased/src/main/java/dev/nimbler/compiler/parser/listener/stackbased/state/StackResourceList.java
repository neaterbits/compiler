package dev.nimbler.compiler.parser.listener.stackbased.state;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackResourceList<RESOURCE> extends ListStackEntry<RESOURCE> {

	public StackResourceList(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
