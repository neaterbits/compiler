package com.neaterbits.compiler.common.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.expression.Expression;

public final class ReturnStatement extends Statement {

	private final Expression expression;
	
	public ReturnStatement(Context context, Expression expression) {
		super(context);

		Objects.requireNonNull(expression);

		this.expression = expression;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onReturnStatement(this, param);
	}
}
