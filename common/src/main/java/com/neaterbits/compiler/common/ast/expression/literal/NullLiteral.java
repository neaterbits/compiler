package com.neaterbits.compiler.common.ast.expression.literal;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.expression.ExpressionVisitor;

public final class NullLiteral extends Literal {

	public NullLiteral(Context context) {
		super(context);
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onNullLiteral(this, param);
	}
}
