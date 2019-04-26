package com.neaterbits.compiler.ast.expression.literal;

import java.util.Objects;

import com.neaterbits.compiler.ast.expression.ExpressionVisitor;
import com.neaterbits.compiler.ast.type.primitive.StringType;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class StringLiteral extends Literal {

	private final String value;
	
	public StringLiteral(Context context, String value, StringType type) {
		super(context, type);

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
