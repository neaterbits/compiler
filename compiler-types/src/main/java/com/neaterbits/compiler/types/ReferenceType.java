package com.neaterbits.compiler.types;

public enum ReferenceType {
    
    NONE(false, false, true),
    
	NAME(false, false, true),
	SCALAR(false, false, true),
	VALUE(false, false, false),
	POINTER(true, false, true),
	REFERENCE(false, true, false),
	POINTER_REFERENCE(true, true, false);
	
	private ReferenceType(boolean hasPointers, boolean hasReference, boolean isLeaf) {
		this.hasPointers = hasPointers;
		this.hasReference = hasReference;
		this.isLeaf = isLeaf;
	}

	private final boolean hasPointers;
	private final boolean hasReference;
	private final boolean isLeaf;
	
	public boolean hasPointers() {
		return hasPointers;
	}
	
	public boolean hasReference() {
		return hasReference;
	}
	
    public boolean isLeaf() {
        return isLeaf;
    }
}
