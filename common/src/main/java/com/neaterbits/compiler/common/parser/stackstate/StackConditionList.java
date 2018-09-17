package com.neaterbits.compiler.common.parser.stackstate;

import com.neaterbits.compiler.common.ast.condition.Condition;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ListStackEntry;

public class StackConditionList extends ListStackEntry<Condition> {

	public StackConditionList(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
