package com.neaterbits.compiler.common.ast.expression;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.ast.operator.Operator;
import com.neaterbits.compiler.common.ast.type.BaseType;

public final class ExpressionList extends Expression {

	private final List<Operator> operators;
	private final ASTList<Expression> expressions;
	
	public ExpressionList(Context context, List<Operator> operators, List<Expression> expressions) {
		super(context);

		Objects.requireNonNull(operators);
		Objects.requireNonNull(expressions);

		if (operators.size() != expressions.size() - 1) {
			throw new IllegalArgumentException("Expected one less operator than expression: " + operators.size() + "/" + expressions.size() + ": " + expressions);
		}

		this.operators 	 = Collections.unmodifiableList(operators);
		this.expressions = makeList(expressions);
	}
	
	public List<Operator> getOperators() {
		return operators;
	}

	public ASTList<Expression> getExpressions() {
		return expressions;
	}
	
	@Override
	public BaseType getType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onExpressionList(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(expressions, recurseMode, iterator);
	}
}
