package com.neaterbits.compiler.common.ast.statement;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.expression.Expression;

public final class ExpressionStatement extends Statement {

	private final Expression expression;
	
	public ExpressionStatement(Context context, Expression expression) {
		super(context);

		this.expression = expression;;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onExpressionStatement(this, param);
	}
}
