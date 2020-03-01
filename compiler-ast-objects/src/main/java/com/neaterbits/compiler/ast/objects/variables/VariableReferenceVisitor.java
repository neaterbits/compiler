package com.neaterbits.compiler.ast.objects.variables;

public interface VariableReferenceVisitor<T, R> {

	R onNameReference(NameReference nameReference, T param);

	R onSimpleVariableReference(SimpleVariableReference variableReference, T param);
	
	R onArrayAccessReference(ArrayAccessReference variableReference, T param);
	
	R onFieldAccessReference(FieldAccessReference fieldAccessReference, T param);

	R onStaticMemberReference(StaticMemberReference staticMemberReference, T param);
	
	R onPrimaryList(PrimaryListVariableReference primaryListVariableReference, T param);
}
