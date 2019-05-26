package com.neaterbits.compiler.util.operator;

public interface Operator {

	public Arity getArity();
	
	public <T, R> R visit(OperatorVisitor<T, R> visitor, T param);

}
