package com.neaterbits.compiler.common.ast.operator;

public enum Relational implements Operator {
	
	EQUALS,
	NOT_EQUALS,
	
	LESS_THAN,
	LESS_THAN_OR_EQUALS,
	
	GREATER_THAN,
	GREATER_THAN_OR_EQUALS;

	@Override
	public <T, R> R visit(OperatorVisitor<T, R> visitor, T param) {
		return visitor.onRelational(this, param);
	}
}
