package com.neaterbits.compiler.common.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.expression.AssignmentExpression;

public final class AssignmentStatement extends Statement {

	private final AssignmentExpression expression;

	public AssignmentStatement(Context context, AssignmentExpression expression) {
		super(context);

		Objects.requireNonNull(expression);

		this.expression = expression;
	}

	public AssignmentExpression getExpression() {
		return expression;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onAssignment(this, param);
	}
}
