package com.neaterbits.compiler.common.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;

public abstract class UnaryExpression extends Expression {

	private final Expression expression;

	public UnaryExpression(Context context, Expression expression) {
		super(context);

		Objects.requireNonNull(expression);

		this.expression = expression;
	}

	public final Expression getExpression() {
		return expression;
	}
}
