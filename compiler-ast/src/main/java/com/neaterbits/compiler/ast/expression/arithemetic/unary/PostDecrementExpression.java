package com.neaterbits.compiler.ast.expression.arithemetic.unary;

import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.expression.ExpressionVisitor;
import com.neaterbits.compiler.ast.expression.UnaryExpression;
import com.neaterbits.compiler.util.Context;

public final class PostDecrementExpression extends UnaryExpression {

	public PostDecrementExpression(Context context, Expression expression) {
		super(context, expression);
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return null;
	}
}
