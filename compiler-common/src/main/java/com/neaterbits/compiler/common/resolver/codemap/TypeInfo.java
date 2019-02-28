package com.neaterbits.compiler.common.resolver.codemap;

import java.util.Objects;

import com.neaterbits.compiler.common.loader.TypeVariant;

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
