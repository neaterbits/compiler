package com.neaterbits.compiler.types.operator;

public enum Bitwise implements NumericOperator {
	AND(Arity.BINARY),
	OR(Arity.BINARY),
	XOR(Arity.BINARY),
	COMPLEMENT(Arity.UNARY),
	LEFTSHIFT(Arity.BINARY),
	RIGHTSHIFT_SIGNED(Arity.BINARY),
	RIGHTSHIFT_UNSIGNED(Arity.BINARY);

	private final Arity arity;
	
	private Bitwise(Arity arity) {
		this.arity = arity;
	}

	@Override
    public OperatorType getOperatorType() {
        return OperatorType.BITWISE;
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
		return visitor.onBitwise(this, param);
	}
}
