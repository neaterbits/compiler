package com.neaterbits.compiler.common.parser.stackstate;

import com.neaterbits.compiler.common.ast.variables.VariableReference;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.StackEntry;
import com.neaterbits.compiler.common.parser.VariableReferenceSetter;

public final class StackAssignmentLHS extends StackEntry implements VariableReferenceSetter {

	private VariableReference variableReference;

	public StackAssignmentLHS(ParseLogger parseLogger) {
		super(parseLogger);
	}

	public VariableReference getVariableReference() {
		return variableReference;
	}

	@Override
	public void setVariableReference(VariableReference variableReference) {
		
		if (this.variableReference != null) {
			throw new IllegalStateException("Variable reference already set");
		}

		this.variableReference = variableReference;
	}
}
