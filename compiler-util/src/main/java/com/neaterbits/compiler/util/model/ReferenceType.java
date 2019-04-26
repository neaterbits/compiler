package com.neaterbits.compiler.util.model;

public enum ReferenceType {
	NAME(false, false),
	SCALAR(false, false),
	VALUE(false, false),
	POINTER(true, false),
	REFERENCE(false, true),
	POINTER_REFERENCE(true, true);
	
	private ReferenceType(boolean hasPointers, boolean hasReference) {
		this.hasPointers = hasPointers;
		this.hasReference = hasReference;
	}

	private final boolean hasPointers;
	private final boolean hasReference;
	
	public boolean hasPointers() {
		return hasPointers;
	}
	public boolean hasReference() {
		return hasReference;
	}
}
