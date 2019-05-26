package com.neaterbits.compiler.util.operator;

public enum Logical implements Operator {
	AND(Arity.BINARY),
	OR(Arity.BINARY),
	NOT(Arity.UNARY);
	
	private final Arity arity;

	private Logical(Arity arity) {
		this.arity = arity;
	}

	@Override
	public Arity getArity() {
		return arity;
	}

	@Override
	public <T, R> R visit(OperatorVisitor<T, R> visitor, T param) {
		return visitor.onLogical(this, param);
	}
}
