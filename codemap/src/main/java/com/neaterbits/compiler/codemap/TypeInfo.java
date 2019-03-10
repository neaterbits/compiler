package com.neaterbits.compiler.codemap;

import java.util.Objects;

public final class TypeInfo {

	private final int typeNo;
	private final TypeVariant typeVariant;

	public TypeInfo(int typeNo, TypeVariant typeVariant) {

		Objects.requireNonNull(typeVariant);
		
		this.typeNo = typeNo;
		this.typeVariant = typeVariant;
	}
	
	public int getTypeNo() {
		return typeNo;
	}

	public TypeVariant getTypeVariant() {
		return typeVariant;
	}
}
