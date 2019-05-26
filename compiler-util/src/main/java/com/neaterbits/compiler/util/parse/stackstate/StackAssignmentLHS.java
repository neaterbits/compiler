package com.neaterbits.compiler.util.parse.stackstate;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.stackstate.setters.VariableReferenceSetter;

public final class StackAssignmentLHS<PRIMARY, VARIABLE_REFERENCE extends PRIMARY, NESTED_EXPRESSION extends PRIMARY>
		extends StackPrimaryList<PRIMARY, VARIABLE_REFERENCE, NESTED_EXPRESSION>
		implements VariableReferenceSetter<VARIABLE_REFERENCE> {

	public StackAssignmentLHS(ParseLogger parseLogger) {
		super(parseLogger);
	}
	
	@Override
	public void setVariableReference(VARIABLE_REFERENCE variableReference) {
		addPrimary(variableReference);
	}
}
