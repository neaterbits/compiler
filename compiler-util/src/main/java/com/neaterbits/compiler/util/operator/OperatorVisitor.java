package com.neaterbits.compiler.util.operator;

public interface OperatorVisitor<T, R> {

	R onArithmetic(Arithmetic arithmetic, T param);
	
	R onBitwise(Bitwise bitwise, T param);
	
	R onRelational(Relational relational, T param);
	
	R onLogical(Logical logical, T param);
}