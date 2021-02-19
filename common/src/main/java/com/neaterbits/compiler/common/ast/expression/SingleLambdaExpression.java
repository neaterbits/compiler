package com.neaterbits.compiler.common.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

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
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onSingleLambdaExpression(this, param);
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		super.doRecurse(recurseMode, visitor);
		
		doIterate(expression, recurseMode, visitor);
	}
}
