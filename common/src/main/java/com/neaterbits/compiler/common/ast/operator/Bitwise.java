package com.neaterbits.compiler.common.ast.operator;

public enum Bitwise implements NumericOperator {
	AND,
	OR,
	XOR,
	COMPLEMENT,
	LEFTSHIFT,
	RIGHTSHIFT;

	@Override
	public <T, R> R visit(OperatorVisitor<T, R> visitor, T param) {
		return visitor.onBitwise(this, param);
	}
}
