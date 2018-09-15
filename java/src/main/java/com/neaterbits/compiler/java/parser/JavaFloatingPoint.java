package com.neaterbits.compiler.java.parser;

import com.neaterbits.compiler.common.ast.expression.Base;

final class JavaFloatingPoint {

	private final double value;
	private final Base base;
	private final int bits;

	public JavaFloatingPoint(double value, Base base, int bits) {
		this.value = value;
		this.base = base;
		this.bits = bits;
	}

	public double getValue() {
		return value;
	}

	public Base getBase() {
		return base;
	}

	public int getBits() {
		return bits;
	}
}
