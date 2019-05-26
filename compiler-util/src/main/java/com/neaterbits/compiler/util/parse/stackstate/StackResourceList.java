package com.neaterbits.compiler.util.parse.stackstate;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.stackstate.base.ListStackEntry;

public final class StackResourceList<RESOURCE> extends ListStackEntry<RESOURCE> {

	public StackResourceList(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
