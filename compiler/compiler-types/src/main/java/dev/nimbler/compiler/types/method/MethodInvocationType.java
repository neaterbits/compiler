package dev.nimbler.compiler.types.method;

public enum MethodInvocationType {
    // UNRESOLVED(false),
	NO_OBJECT(false), // May be on this or on static-method if called from a static context
	NAMED_CLASS_STATIC(true),
	// NAMED_CLASS_STATIC_OR_STATIC_VAR(true),
	VARIABLE_REFERENCE(false),
	PRIMARY(false),
	SUPER(false),
	TYPED_SUPER(true),
	
	SUB(false);

	private final boolean requiresClassName;

	private MethodInvocationType(boolean requiresClassName) {
		this.requiresClassName = requiresClassName;
	}

	public boolean requiresClassName() {
		return requiresClassName;
	}
}

