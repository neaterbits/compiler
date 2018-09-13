package com.neaterbits.compiler.common.ast.expression.arithemetic.unary;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.expression.ExpressionVisitor;
import com.neaterbits.compiler.common.ast.expression.UnaryExpression;

public final class PostDecrementExpression extends UnaryExpression {

	public PostDecrementExpression(Context context, Expression expression) {
		super(context, expression);
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return null;
	}
}
