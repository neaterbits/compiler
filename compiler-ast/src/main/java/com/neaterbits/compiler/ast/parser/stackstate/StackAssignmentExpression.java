package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.expression.VariableExpression;
import com.neaterbits.compiler.ast.expression.literal.Primary;
import com.neaterbits.compiler.ast.parser.ExpressionSetter;
import com.neaterbits.compiler.ast.parser.PrimarySetter;
import com.neaterbits.compiler.ast.parser.StackEntry;
import com.neaterbits.compiler.ast.parser.VariableReferenceSetter;
import com.neaterbits.compiler.ast.variables.VariableReference;
import com.neaterbits.compiler.util.parse.ParseLogger;

public class StackAssignmentExpression extends StackEntry
	implements ExpressionSetter,
			   VariableReferenceSetter,
			   PrimarySetter {

	private VariableReference lhs;
	private Expression rhs;

	public StackAssignmentExpression(ParseLogger parseLogger) {
		super(parseLogger);
	}

	public VariableReference getLHS() {
		return lhs;
	}

	public void setLHS(VariableReference lhs) {
		
		Objects.requireNonNull(lhs);
		
		this.lhs = lhs;
	}

	public Expression getRHS() {
		return rhs;
	}

	public void setRHS(Expression rhs) {
		
		Objects.requireNonNull(rhs);
		
		this.rhs = rhs;
	}
	
	@Override
	public void setVariableReference(VariableReference variableReference) {

		addExpression(new VariableExpression(variableReference.getContext(), variableReference));
	}

	@Override
	public void addPrimary(Primary primary) {
		if (this.lhs == null) {
			throw new IllegalStateException("Invoked only for rhs");
		}

		addExpression(primary);
	}

	@Override
	public void addExpression(Expression expression) {

		if (this.rhs != null) {
			throw new IllegalStateException("rhs already set");
		}

		this.rhs = expression;
	}
}
