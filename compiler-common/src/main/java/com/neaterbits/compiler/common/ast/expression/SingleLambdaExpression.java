package com.neaterbits.compiler.common.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.list.ASTSingle;
import com.neaterbits.compiler.common.ast.type.BaseType;

public final class SingleLambdaExpression extends LambdaExpression {

	private final ASTSingle<Expression> expression;
	
	public SingleLambdaExpression(Context context, LambdaExpressionParameters parameters, Expression expression) {
		super(context, parameters);
		
		Objects.requireNonNull(expression);

		this.expression = makeSingle(expression);
	}

	public Expression getExpression() {
		return expression.get();
	}

	@Override
	public BaseType getType() {
		return expression.get().getType();
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onSingleLambdaExpression(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		super.doRecurse(recurseMode, iterator);
		
		doIterate(expression, recurseMode, iterator);
	}
}
