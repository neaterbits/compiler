package com.neaterbits.compiler.common.ast.expression;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.operator.Operator;

public final class ExpressionList extends Expression {

	private final List<Operator> operators;
	private final List<Expression> expressions;
	
	public ExpressionList(Context context, List<Operator> operators, List<Expression> expressions) {
		super(context);

		Objects.requireNonNull(operators);
		Objects.requireNonNull(expressions);

		if (operators.size() != expressions.size() - 1) {
			throw new IllegalArgumentException("Expected one less operator than expression: " + operators.size() + "/" + expressions.size() + ": " + expressions);
		}

		this.operators 	 = Collections.unmodifiableList(operators);
		this.expressions = Collections.unmodifiableList(expressions);
	}
	
	public List<Operator> getOperators() {
		return operators;
	}

	public List<Expression> getExpressions() {
		return expressions;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onExpressionList(this, param);
	}
}
