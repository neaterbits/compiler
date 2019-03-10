package com.neaterbits.compiler.ast.operator;

public interface Operator {

	public Arity getArity();
	
	public <T, R> R visit(OperatorVisitor<T, R> visitor, T param);

}
