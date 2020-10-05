package com.neaterbits.compiler.ast.objects.expression.arithemetic.unary;

import com.neaterbits.compiler.ast.objects.expression.Expression;
import com.neaterbits.compiler.ast.objects.expression.ExpressionVisitor;
import com.neaterbits.compiler.ast.objects.expression.UnaryExpression;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.types.operator.IncrementDecrement;
import com.neaterbits.compiler.types.operator.Operator;
import com.neaterbits.util.parse.context.Context;

public class PreIncrementExpression extends UnaryExpression {

	public PreIncrementExpression(Context context, Expression expression) {
		super(context, expression);
	}

	@Override
    public Operator getOperator() {
        return IncrementDecrement.PRE_INCREMENT;
    }

    @Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.PRE_INCREMENT_EXPRESSION;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onPreIncrement(this, param);
	}
}
