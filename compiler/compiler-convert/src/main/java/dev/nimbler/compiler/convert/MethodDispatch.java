package dev.nimbler.compiler.convert;

public enum MethodDispatch {

	STATIC(true, false, false),
	VTABLE(false, true, false),
	
	NON_OVERRIDABLE(false, false, false),
	ONE_IMPLEMENTATION(false, false, true),
	
	FEW_IMPLEMENTATIONS(false, false, true),
	
	MANY_IMPLEMENTATIONS(false, true, true)
	
	;
	
	private final boolean classMethod;
	private final boolean requiresVTable;
	private final boolean requiresVTableOnClassesAddedDynamically;

	private MethodDispatch(boolean classMethod, boolean requiresVTable, boolean requiresVTableOnClassesAddedDynamically) {
		this.classMethod = classMethod;
		this.requiresVTable = requiresVTable;
		this.requiresVTableOnClassesAddedDynamically = requiresVTableOnClassesAddedDynamically;
	}

	public boolean isClassMethod() {
		return classMethod;
	}

	public boolean requiresVTable() {
		return requiresVTable;
	}

	public boolean requiresVTableOnClassesAddedDynamically() {
		return requiresVTableOnClassesAddedDynamically;
	}
}
