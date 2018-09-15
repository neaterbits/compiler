package com.neaterbits.compiler.common.ast.expression.literal;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.expression.ExpressionVisitor;

public final class BooleanLiteral extends Literal {

	private final boolean value;
	
	public BooleanLiteral(Context context, boolean value) {
		super(context);

		this.value = value;
	}

	public boolean getValue() {
		return value;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onBooleanLiteral(this, param);
	}
}
