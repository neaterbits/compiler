package com.neaterbits.compiler.resolver.types;

import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.util.ScopedName;

public final class TypeSpec extends BaseTypeInfo {

	public TypeSpec(ScopedName scopedName, TypeVariant typeVariant) {
		super(scopedName, typeVariant);
	}
}
