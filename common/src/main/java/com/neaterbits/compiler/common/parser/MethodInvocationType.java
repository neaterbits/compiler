package com.neaterbits.compiler.common.parser;

public enum MethodInvocationType {
	NO_OBJECT(false), // May be on this or on static-method if called from a static context
	NAMED_CLASS_STATIC(true),
	VARIABLE_REFERENCE(false),
	EXPRESSION(false),
	SUPER(false),
	TYPED_SUPER(true);

	private final boolean requiresClassName;

	private MethodInvocationType(boolean requiresClassName) {
		this.requiresClassName = requiresClassName;
	}

	public boolean requiresClassName() {
		return requiresClassName;
	}
}

