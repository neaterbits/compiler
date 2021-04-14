package dev.nimbler.compiler.model.common;

import dev.nimbler.language.common.types.TypeName;

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
