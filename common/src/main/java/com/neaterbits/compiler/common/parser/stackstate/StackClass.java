package com.neaterbits.compiler.common.parser.stackstate;

import com.neaterbits.compiler.common.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ListStackEntry;

public abstract class StackClass extends ListStackEntry<ComplexMemberDefinition> {

	public StackClass(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
