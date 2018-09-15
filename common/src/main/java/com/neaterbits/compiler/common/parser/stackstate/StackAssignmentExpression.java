package com.neaterbits.compiler.common.parser.stackstate;

import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.variables.VariableReference;
import com.neaterbits.compiler.common.parser.StackEntry;

public class StackAssignmentExpression extends StackEntry {

	private VariableReference lhs;
	private Expression rhs;

	public VariableReference getLHS() {
		return lhs;
	}

	public void setLHS(VariableReference lhs) {
		this.lhs = lhs;
	}

	public Expression getRHS() {
		return rhs;
	}

	public void setRHS(Expression rhs) {
		this.rhs = rhs;
	}
}
