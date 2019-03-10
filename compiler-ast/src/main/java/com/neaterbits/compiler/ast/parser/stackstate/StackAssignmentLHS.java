package com.neaterbits.compiler.ast.parser.stackstate;

import com.neaterbits.compiler.ast.parser.VariableReferenceSetter;
import com.neaterbits.compiler.ast.variables.VariableReference;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

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
