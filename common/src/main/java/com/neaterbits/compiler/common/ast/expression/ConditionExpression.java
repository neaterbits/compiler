package com.neaterbits.compiler.common.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.condition.Condition;

public final class ConditionExpression extends Expression {

	private final Condition condition;

	public ConditionExpression(Context context, Condition condition) {
		super(context);
		
		Objects.requireNonNull(condition);
		
		this.condition = condition;
	}

	public Condition getCondition() {
		return condition;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onConditionExpression(this, param);
	}
}
