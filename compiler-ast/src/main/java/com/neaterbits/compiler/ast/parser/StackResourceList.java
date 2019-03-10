package com.neaterbits.compiler.ast.parser;

import com.neaterbits.compiler.ast.expression.Resource;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackResourceList extends ListStackEntry<Resource> {

	public StackResourceList(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
