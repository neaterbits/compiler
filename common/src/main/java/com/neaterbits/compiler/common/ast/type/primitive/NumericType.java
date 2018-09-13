package com.neaterbits.compiler.common.ast.type.primitive;

import com.neaterbits.compiler.common.ast.type.TypeName;

public abstract class NumericType extends ScalarType {
	
	private final int numBits;
	private final boolean signed;

	public abstract boolean isInteger();
	
	protected NumericType(TypeName name, boolean nullable, int numBits, boolean signed) {
		super(name, nullable);

		this.numBits = numBits;
		this.signed = signed;
	}

	public final boolean isFloatingPoint() {
		return !isInteger();
	}

	public final int getNumBits() {
		return numBits;
	}

	public final boolean isSigned() {
		return signed;
	}
}
