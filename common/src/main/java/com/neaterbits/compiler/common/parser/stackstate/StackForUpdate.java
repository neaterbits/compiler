package com.neaterbits.compiler.common.parser.stackstate;

import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ListStackEntry;

public class StackForUpdate extends ListStackEntry<Expression> {

	public StackForUpdate(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
