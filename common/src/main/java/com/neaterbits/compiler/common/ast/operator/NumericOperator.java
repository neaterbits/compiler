package com.neaterbits.compiler.common.ast.operator;

import java.util.Objects;

public class NumericOperator {

	private final Arithmetic arithmetic;
	private final Bitwise bitwise;
	
	public NumericOperator(Arithmetic arithmetic) {
		
		Objects.requireNonNull(arithmetic);
		
		this.arithmetic = arithmetic;
		this.bitwise = null;
	}

	public NumericOperator(Bitwise bitwise) {
		
		Objects.requireNonNull(bitwise);
		
		this.bitwise = bitwise;
		this.arithmetic = null;
	}

	public Arithmetic getArithmetic() {
		return arithmetic;
	}

	public Bitwise getBitwise() {
		return bitwise;
	}
}
