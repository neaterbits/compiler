package com.neaterbits.compiler.ast.expression.literal;

import java.math.BigInteger;
import java.util.Objects;

import com.neaterbits.compiler.ast.expression.Base;
import com.neaterbits.compiler.ast.expression.ExpressionVisitor;
import com.neaterbits.compiler.ast.type.primitive.IntegerType;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class IntegerLiteral extends Literal {

	private final BigInteger value;
	private final Base base;
	private final boolean signed;
	private final int bits;

	public IntegerLiteral(Context context, long value, boolean signed, int bits, IntegerType type) {
		this(context, BigInteger.valueOf(value), Base.DECIMAL, signed, bits, type);
	}

	public IntegerLiteral(Context context, BigInteger value, Base base, boolean signed, int bits, IntegerType type) {
		super(context, type);

		Objects.requireNonNull(value);
		Objects.requireNonNull(base);
		
		if (bits != type.getNumBits()) {
			throw new IllegalArgumentException("Bits mismatch");
		}

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
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.INTEGER_LITERAL;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onIntegerLiteral(this, param);
	}
}
