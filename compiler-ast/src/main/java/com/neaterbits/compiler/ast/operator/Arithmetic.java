package com.neaterbits.compiler.ast.operator;

public enum Arithmetic implements NumericOperator {
	PLUS(Arity.BINARY),
	MINUS(Arity.BINARY),
	MULTIPLY(Arity.BINARY),
	DIVIDE(Arity.BINARY),
	MODULUS(Arity.BINARY),

	INCREMENT(Arity.UNARY),
	DECREMENT(Arity.UNARY);

	private final Arity arity;

	private Arithmetic(Arity arity) {
		this.arity = arity;
	}

	@Override
	public Arity getArity() {
		return arity;
	}



	@Override
	public <T, R> R visit(OperatorVisitor<T, R> visitor, T param) {
		return visitor.onArithmetic(this, param);
	}
}
