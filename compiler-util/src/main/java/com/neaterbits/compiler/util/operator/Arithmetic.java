package com.neaterbits.compiler.util.operator;

public enum Arithmetic implements NumericOperator {
	PLUS(Arity.BINARY),
	MINUS(Arity.BINARY),
	MULTIPLY(Arity.BINARY),
	DIVIDE(Arity.BINARY),
	MODULUS(Arity.BINARY);

	private final Arity arity;

	private Arithmetic(Arity arity) {
		this.arity = arity;
	}

	@Override
    public OperatorType getOperatorType() {
        return OperatorType.ARITHMETIC;
    }

    @Override
    public Enum<?> getEnumValue() {
        return this;
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
