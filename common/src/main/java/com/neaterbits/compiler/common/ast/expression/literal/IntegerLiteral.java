package com.neaterbits.compiler.common.ast.expression.literal;

import java.math.BigInteger;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.expression.Base;
import com.neaterbits.compiler.common.ast.expression.ExpressionVisitor;
import com.neaterbits.compiler.common.ast.type.primitive.IntegerType;

public final class IntegerLiteral extends Literal {

	private final BigInteger value;
	private final Base base;
	private final boolean signed;
	private final int bits;
	
	public IntegerLiteral(Context context, BigInteger value, Base base, boolean signed, int bits, IntegerType type) {
		super(context, type);

		Objects.requireNonNull(value);
		Objects.requireNonNull(base);

		this.value = value;
		this.base = base;
		this.signed = signed;
		this.bits = bits;
	}

	public BigInteger getValue() {
		return value;
	}

	public Base getBase() {
		return base;
	}

	public boolean isSigned() {
		return signed;
	}

	public int getBits() {
		return bits;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onIntegerLiteral(this, param);
	}
}
