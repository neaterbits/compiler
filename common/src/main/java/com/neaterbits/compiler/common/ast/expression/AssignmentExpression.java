package com.neaterbits.compiler.common.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.variables.VariableReference;

public class AssignmentExpression extends Expression {
	private final VariableReference variable;
	private final Expression expression;
	
	public AssignmentExpression(Context context, VariableReference variable, Expression expression) {
		super(context);
		
		Objects.requireNonNull(variable);
		Objects.requireNonNull(expression);
		
		this.variable = variable;
		this.expression = expression;
	}

	public VariableReference getVariable() {
		return variable;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onAssignment(this, param);
	}
}
