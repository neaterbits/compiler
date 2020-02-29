package com.neaterbits.compiler.util.parse.listener.stackbased.state;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.base.ListStackEntry;

public final class StackResourceList<RESOURCE> extends ListStackEntry<RESOURCE> {

	public StackResourceList(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
