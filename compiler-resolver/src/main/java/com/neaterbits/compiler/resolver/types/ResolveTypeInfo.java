package com.neaterbits.compiler.resolver.types;

import com.neaterbits.build.types.ScopedName;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.types.TypeVariant;

public interface ResolveTypeInfo {
	
	TypeName getTypeName();
	
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
