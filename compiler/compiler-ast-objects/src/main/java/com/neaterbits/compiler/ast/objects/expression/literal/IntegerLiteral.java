package com.neaterbits.compiler.ast.objects.expression.literal;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.expression.ExpressionVisitor;
import com.neaterbits.compiler.ast.objects.type.primitive.IntegerType;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Base;
import com.neaterbits.util.parse.context.Context;

public final class IntegerLiteral extends Literal {

	private final long value;
	private final Base base;
	private final boolean signed;
	private final int bits;

	public IntegerLiteral(Context context, long value, boolean signed, int bits, IntegerType type, int typeNo) {
		this(context, value, Base.DECIMAL, signed, bits, type, typeNo);
	}

	public IntegerLiteral(Context context, long value, Base base, boolean signed, int bits, IntegerType type, int typeNo) {
		super(context, type, typeNo);

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

	public long getValue() {
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
