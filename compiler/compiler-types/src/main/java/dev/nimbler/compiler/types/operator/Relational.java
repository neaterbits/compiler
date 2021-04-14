package dev.nimbler.compiler.types.operator;

public enum Relational implements Operator {
	
	EQUALS(Arity.BINARY),
	NOT_EQUALS(Arity.BINARY),
	
	LESS_THAN(Arity.BINARY),
	LESS_THAN_OR_EQUALS(Arity.BINARY),
	
	GREATER_THAN(Arity.BINARY),
	GREATER_THAN_OR_EQUALS(Arity.BINARY);
	
	private final Arity arity;

	private Relational(Arity arity) {
		this.arity = arity;
	}

	@Override
    public OperatorType getOperatorType() {
        return OperatorType.RELATIONAL;
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
		return visitor.onRelational(this, param);
	}
}
