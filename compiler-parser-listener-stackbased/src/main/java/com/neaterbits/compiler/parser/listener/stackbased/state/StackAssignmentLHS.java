package com.neaterbits.compiler.parser.listener.stackbased.state;

import com.neaterbits.compiler.parser.listener.stackbased.state.setters.VariableReferenceSetter;
import com.neaterbits.compiler.util.parse.ParseLogger;

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
