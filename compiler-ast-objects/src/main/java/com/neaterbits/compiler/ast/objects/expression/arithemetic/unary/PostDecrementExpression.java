package com.neaterbits.compiler.ast.objects.expression.arithemetic.unary;

import com.neaterbits.compiler.ast.objects.expression.Expression;
import com.neaterbits.compiler.ast.objects.expression.ExpressionVisitor;
import com.neaterbits.compiler.ast.objects.expression.UnaryExpression;
import com.neaterbits.compiler.types.operator.IncrementDecrement;
import com.neaterbits.compiler.types.operator.Operator;
import com.neaterbits.util.parse.context.Context;

public final class PostDecrementExpression extends UnaryExpression {

	public PostDecrementExpression(Context context, Expression expression) {
		super(context, expression);
	}
	
	@Override
    public Operator getOperator() {
        return IncrementDecrement.POST_DECREMENT;
    }

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return null;
	}
}
