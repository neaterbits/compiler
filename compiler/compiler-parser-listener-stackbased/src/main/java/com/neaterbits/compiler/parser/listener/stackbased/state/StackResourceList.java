package com.neaterbits.compiler.parser.listener.stackbased.state;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackResourceList<RESOURCE> extends ListStackEntry<RESOURCE> {

	public StackResourceList(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
