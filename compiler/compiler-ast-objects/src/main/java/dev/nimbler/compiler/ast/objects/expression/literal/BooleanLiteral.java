package dev.nimbler.compiler.ast.objects.expression.literal;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.expression.ExpressionVisitor;
import dev.nimbler.compiler.ast.objects.type.primitive.BooleanType;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class BooleanLiteral extends Literal {

	private final boolean value;

	public BooleanLiteral(Context context, boolean value, BooleanType booleanType, int typeNo) {
		super(context, booleanType, typeNo);

		this.value = value;
	}

	public boolean getValue() {
		return value;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.BOOLEAN_LITERAL;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onBooleanLiteral(this, param);
	}
}
