package com.neaterbits.compiler.common.loader;

import com.neaterbits.compiler.common.ast.ScopedName;

public final class TypeSpec extends BaseTypeInfo {

	public TypeSpec(ScopedName scopedName, TypeVariant typeVariant) {
		super(scopedName, typeVariant);
	}
}
