package com.neaterbits.compiler.ast.objects.expression.arithemetic.unary;

import com.neaterbits.compiler.ast.objects.expression.Expression;
import com.neaterbits.compiler.ast.objects.expression.ExpressionVisitor;
import com.neaterbits.compiler.ast.objects.expression.UnaryExpression;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.operator.IncrementDecrement;
import com.neaterbits.compiler.util.operator.Operator;

public final class PostDecrementExpression extends UnaryExpression {

	public PostDecrementExpression(Context context, Expression expression) {
		super(context, expression);
	}
	
	@Override
    public Operator getOperator() {
        return IncrementDecrement.POST_DECREMENT;
    }

    @Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.POST_DECREMENT_EXPRESSION;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return null;
	}
}
