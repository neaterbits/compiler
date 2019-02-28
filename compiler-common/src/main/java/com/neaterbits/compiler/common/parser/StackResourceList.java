package com.neaterbits.compiler.common.parser;

import com.neaterbits.compiler.common.ast.expression.Resource;
import com.neaterbits.compiler.common.log.ParseLogger;

public final class StackResourceList extends ListStackEntry<Resource> {

	public StackResourceList(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
