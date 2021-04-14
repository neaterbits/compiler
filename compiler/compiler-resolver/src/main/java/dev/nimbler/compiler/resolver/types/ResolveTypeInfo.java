package dev.nimbler.compiler.resolver.types;

import dev.nimbler.language.common.types.ScopedName;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.types.TypeVariant;

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
