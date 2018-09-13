package com.neaterbits.compiler.common.ast.statement;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.variables.VariableReference;

public final class AssignmentStatement extends Statement {

	private final List<VariableReference> variables;
	private final Expression expression;
	
	public AssignmentStatement(Context context, List<VariableReference> variables, Expression expression) {
		super(context);
		
		Objects.requireNonNull(variables);
		
		if (variables.isEmpty()) {
			throw new IllegalArgumentException("requires at least one variable");
		}
		
		Objects.requireNonNull(expression);
		
		this.variables = Collections.unmodifiableList(variables);
		this.expression = expression;
	}

	public List<VariableReference> getVariables() {
		return variables;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onAssignment(this, param);
	}
}
