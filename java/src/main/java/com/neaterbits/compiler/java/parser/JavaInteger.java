package com.neaterbits.compiler.java.parser;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.expression.Base;

final class JavaInteger {
	
	private final long value;
	private final Base base;
	private final int bits;
	
	public JavaInteger(long value, Base base, int bits) {

		Objects.requireNonNull(base);

		this.value = value;
		this.base = base;
		this.bits = bits;
	}

	public long getValue() {
		return value;
	}

	public Base getBase() {
		return base;
	}

	public int getBits() {
		return bits;
	}
}
