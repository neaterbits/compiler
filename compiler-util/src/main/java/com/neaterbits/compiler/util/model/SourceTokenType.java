package com.neaterbits.compiler.util.model;

public enum SourceTokenType {
	
	KEYWORD(false, false, false),
	
	CLASS_DECLARATION_NAME(false, true, false),
	INTERFACE_DECLARATION_NAME(false, true, false),
	METHOD_DECLARATION_NAME(false, false, false),
	
	CLASS_REFERENCE_NAME(false, false, true),
	METHOD_REFERENCE_NAME(false, false, true),
	
	INSTANCE_VARIABLE(true, false, false),
	LOCAL_VARIABLE(true, false, false),
	
	CALL_PARAMETER(true, false, false),
	
	NAMESPACE_DECLARATION_NAME(false, false, false),

	IMPORT_NAME(false, false, false),

	UNKNOWN(false, false, false);
	
	
	private final boolean variable;
	private final boolean typeDeclarationName;
	private final boolean typeName;

	private SourceTokenType(boolean variable, boolean typeDeclarationName, boolean typeName) {
		this.variable = variable;
		this.typeDeclarationName = typeDeclarationName;
		this.typeName = typeName;
	}

	public boolean isKeyword() {
		return this == KEYWORD;
	}
	
	public boolean isVariable() {
		return variable;
	}

	public boolean isTypeDeclarationName() {
		return typeDeclarationName;
	}

	public boolean isTypeName() {
		return typeName;
	}
}
