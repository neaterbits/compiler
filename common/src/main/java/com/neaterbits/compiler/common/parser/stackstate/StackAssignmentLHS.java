package com.neaterbits.compiler.common.parser.stackstate;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.variables.VariableReference;
import com.neaterbits.compiler.common.log.ParseLogger;

public final class StackAssignmentLHS extends StackPrimaryList  {

	public StackAssignmentLHS(ParseLogger parseLogger) {
		super(parseLogger);
	}
	
	public VariableReference getVariableReference(Context context) {
		return makeVariableReference(context);
	}
}
