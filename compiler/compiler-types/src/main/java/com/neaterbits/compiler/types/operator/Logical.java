package com.neaterbits.compiler.types.operator;

public enum Logical implements Operator, UnaryOperator {
	AND(Arity.BINARY),
	OR(Arity.BINARY),
	NOT(Arity.UNARY);
	
	private final Arity arity;

	private Logical(Arity arity) {
		this.arity = arity;
	}

	@Override
    public OperatorType getOperatorType() {
        return OperatorType.LOGICAL;
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
    public Notation getNotation() {

	    final Notation notation;
	    
	    if (this == NOT) {
	        notation = Notation.PREFIX;
	    }
	    else {
	        throw new UnsupportedOperationException();
	    }

	    return notation;
    }

    @Override
	public <T, R> R visit(OperatorVisitor<T, R> visitor, T param) {
		return visitor.onLogical(this, param);
	}
}
