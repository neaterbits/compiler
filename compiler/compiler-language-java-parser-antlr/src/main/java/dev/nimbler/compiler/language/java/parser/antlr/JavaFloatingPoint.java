package dev.nimbler.compiler.language.java.parser.antlr;

import dev.nimbler.compiler.util.Base;

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
