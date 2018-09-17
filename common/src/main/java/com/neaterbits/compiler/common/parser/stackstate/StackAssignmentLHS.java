package com.neaterbits.compiler.common.parser.stackstate;

import com.neaterbits.compiler.common.ast.variables.VariableReference;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.StackEntry;

public final class StackAssignmentLHS extends StackEntry {

	private VariableReference variableReference;

	public StackAssignmentLHS(ParseLogger parseLogger) {
		super(parseLogger);
	}

	public VariableReference getVariableReference() {
		return variableReference;
	}

	public void setVariableReference(VariableReference variableReference) {
		this.variableReference = variableReference;
	}
}
