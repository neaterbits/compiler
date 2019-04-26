package com.neaterbits.compiler.ast.expression.arithemetic.unary;

import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.expression.ExpressionVisitor;
import com.neaterbits.compiler.ast.expression.UnaryExpression;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class PreDecrementExpression extends UnaryExpression {

	public PreDecrementExpression(Context context, Expression expression) {
		super(context, expression);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.PRE_DECREMENT_EXPRESSION;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onPreDecrement(this, param);
	}
}
