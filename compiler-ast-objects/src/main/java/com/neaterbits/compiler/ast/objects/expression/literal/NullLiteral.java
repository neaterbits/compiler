package com.neaterbits.compiler.ast.objects.expression.literal;

import com.neaterbits.compiler.ast.objects.expression.ExpressionVisitor;
import com.neaterbits.compiler.ast.objects.type.primitive.NullType;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.parse.context.Context;

public final class NullLiteral extends Literal {

	public NullLiteral(Context context) {
		super(context, NullType.INSTANCE, -1);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.NULL_LITERAL;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onNullLiteral(this, param);
	}
}
