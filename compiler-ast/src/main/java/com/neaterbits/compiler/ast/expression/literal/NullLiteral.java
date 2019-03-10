package com.neaterbits.compiler.ast.expression.literal;

import com.neaterbits.compiler.ast.expression.ExpressionVisitor;
import com.neaterbits.compiler.ast.type.primitive.NullType;
import com.neaterbits.compiler.util.Context;

public final class NullLiteral extends Literal {

	public NullLiteral(Context context) {
		super(context, NullType.INSTANCE);
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onNullLiteral(this, param);
	}
}
