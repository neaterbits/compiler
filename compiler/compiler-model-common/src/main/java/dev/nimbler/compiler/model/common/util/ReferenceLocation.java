package dev.nimbler.compiler.model.common.util;

@Deprecated
public enum ReferenceLocation {

	EXTENDS_FROM,
	STATIC_OR_STATIC_INSTANCE_METHOD_CALL,
	FIELD,
	RETURNTYPE,
	PARAMETER,
	VARIABLE_INITIALIZER,
	CATCH_EXCEPTION,
	CLASS_INSTANCE_CREATION;

}
