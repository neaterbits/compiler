package com.neaterbits.compiler.ast.objects.expression.literal;

import java.math.BigDecimal;
import java.util.Objects;

import com.neaterbits.compiler.ast.objects.expression.ExpressionVisitor;
import com.neaterbits.compiler.ast.objects.type.primitive.FloatingPointType;
import com.neaterbits.compiler.util.Base;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class FloatingPointLiteral extends Literal {

	private final BigDecimal value;
	private final Base base;
	private final int bits;

	public FloatingPointLiteral(Context context, BigDecimal value, Base base, int bits, FloatingPointType type) {
		super(context, type);

		Objects.requireNonNull(value);
		Objects.requireNonNull(base);

		this.value = value;
		this.base = base;
		this.bits = bits;
	}

	public BigDecimal getValue() {
		return value;
	}

	public Base getBase() {
		return base;
	}

	public int getBits() {
		return bits;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.FLOATING_POINT_LITERAL;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onFloatingPointLiteral(this, param);
	}
}
