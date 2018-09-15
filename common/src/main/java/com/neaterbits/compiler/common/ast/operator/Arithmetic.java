package com.neaterbits.compiler.common.ast.operator;

public enum Arithmetic implements NumericOperator {
	PLUS,
	MINUS,
	MULTIPLY,
	DIVIDE,
	MODULUS,
	
	INCREMENT,
	DECREMENT;

	@Override
	public <T, R> R visit(OperatorVisitor<T, R> visitor, T param) {
		return visitor.onArithmetic(this, param);
	}
}
