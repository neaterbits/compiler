package dev.nimbler.compiler.ast.objects.expression.literal;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.expression.ExpressionVisitor;
import dev.nimbler.compiler.ast.objects.type.primitive.StringType;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class StringLiteral extends Literal {

	private final String value;

	public StringLiteral(Context context, String value, StringType type, int typeNo) {
		super(context, type, typeNo);

		Objects.requireNonNull(value);

		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.STRING_LITERAL;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onStringLiteral(this, param);
	}
}
