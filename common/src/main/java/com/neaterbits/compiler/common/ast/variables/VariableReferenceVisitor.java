package com.neaterbits.compiler.common.ast.variables;

public interface VariableReferenceVisitor<T, R> {

	R onSimpleVariableReference(SimpleVariableReference variableReference, T param);
	
	R onArrayAccessReference(ArrayAccessReference variableReference, T param);
	
	R onFieldAccessReference(FieldAccessReference fieldAccessReference, T param);
	
}
