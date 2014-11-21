package com.neaterbits.compiler.common.ast.expression.literal;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.expression.ExpressionVisitor;
import com.neaterbits.compiler.common.ast.type.primitive.BooleanType;

public final class BooleanLiteral extends Literal {

	private final boolean value;
	
	public BooleanLiteral(Context context, boolean value, BooleanType booleanType) {
		super(context, booleanType);

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
