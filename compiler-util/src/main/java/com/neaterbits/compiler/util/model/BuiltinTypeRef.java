package com.neaterbits.compiler.util.model;

import com.neaterbits.compiler.util.TypeName;

public final class BuiltinTypeRef extends BaseTypeRef {

	private final boolean isScalar;
	
	public BuiltinTypeRef(TypeName typeName, boolean isScalar) {
		super(typeName);
		
		this.isScalar = isScalar;
	}

	public boolean isScalar() {
		return isScalar;
	}
}
