package com.neaterbits.compiler.common.ast.expression;

public enum Base {
	BINARY(2),
	OCTAL(8),
	DECIMAL(10),
	HEX(16);
	
	private final int radix;

	private Base(int radix) {
		this.radix = radix;
	}

	public int getRadix() {
		return radix;
	}
}
