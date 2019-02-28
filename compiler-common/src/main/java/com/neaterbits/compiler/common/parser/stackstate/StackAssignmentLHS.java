package com.neaterbits.compiler.common.parser.stackstate;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.variables.VariableReference;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.VariableReferenceSetter;

public final class StackAssignmentLHS extends StackPrimaryList implements VariableReferenceSetter {

	public StackAssignmentLHS(ParseLogger parseLogger) {
		super(parseLogger);
	}
	
	public VariableReference getVariableReference(Context context) {
		return makeVariableReference(context);
	}

	@Override
	public void setVariableReference(VariableReference variableReference) {
		addPrimary(variableReference);
	}
}
