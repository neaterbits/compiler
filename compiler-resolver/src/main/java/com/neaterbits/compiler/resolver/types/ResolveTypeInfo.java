package com.neaterbits.compiler.resolver.types;

import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.util.ScopedName;

public interface ResolveTypeInfo {
	
	ScopedName getScopedName();
	
	TypeVariant getTypeVariant();

	default boolean isInterface() {
		return getTypeVariant() == TypeVariant.INTERFACE;
	}
	
	default boolean isClass() {
		return getTypeVariant() == TypeVariant.CLASS;
	}
	
	default boolean isEnum() {
		return getTypeVariant() == TypeVariant.ENUM;
	}
}
