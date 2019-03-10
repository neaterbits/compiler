package com.neaterbits.compiler.resolver;

public enum ReferenceType {

	EXTENDS_FROM,
	STATIC_OR_STATIC_INSTANCE_METHOD_CALL,
	FIELD,
	RETURNTYPE,
	PARAMETER,
	VARIABLE_INITIALIZER,
	CATCH_EXCEPTION;
	
}
