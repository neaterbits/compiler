package com.neaterbits.compiler.util.parse.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.base.StackEntry;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.setters.ExpressionSetter;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.setters.PrimarySetter;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.setters.VariableReferenceSetter;

public class StackAssignmentExpression<EXPRESSION, PRIMARY extends EXPRESSION, VARIABLE_REFERENCE extends PRIMARY> extends StackEntry
	implements ExpressionSetter<EXPRESSION>,
			   VariableReferenceSetter<VARIABLE_REFERENCE>,
			   PrimarySetter<PRIMARY> {

	private VARIABLE_REFERENCE lhs;
	private EXPRESSION rhs;

	public StackAssignmentExpression(ParseLogger parseLogger) {
		super(parseLogger);
	}

	public VARIABLE_REFERENCE getLHS() {
		return lhs;
	}

	public void setLHS(VARIABLE_REFERENCE lhs) {
		
		Objects.requireNonNull(lhs);
		
		this.lhs = lhs;
	}

	public EXPRESSION getRHS() {
		return rhs;
	}

	public void setRHS(EXPRESSION rhs) {
		
		Objects.requireNonNull(rhs);
		
		this.rhs = rhs;
	}
	
	@Override
	public void setVariableReference(VARIABLE_REFERENCE variableReference) {

		addExpression(variableReference);
	}

	@Override
	public void addPrimary(PRIMARY primary) {
		if (this.lhs == null) {
			throw new IllegalStateException("Invoked only for rhs");
		}

		addExpression(primary);
	}

	@Override
	public void addExpression(EXPRESSION expression) {

		if (this.rhs != null) {
			throw new IllegalStateException("rhs already set");
		}

		this.rhs = expression;
	}
}
