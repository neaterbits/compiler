package com.neaterbits.compiler.ast.objects.expression.arithemetic.unary;

import com.neaterbits.compiler.ast.objects.expression.Expression;
import com.neaterbits.compiler.ast.objects.expression.ExpressionVisitor;
import com.neaterbits.compiler.ast.objects.expression.UnaryExpression;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class PostIncrementExpression extends UnaryExpression {

	public PostIncrementExpression(Context context, Expression expression) {
		super(context, expression);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.POST_INCREMENT_EXPRESSION;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onPostIncrement(this, param);
	}
}
