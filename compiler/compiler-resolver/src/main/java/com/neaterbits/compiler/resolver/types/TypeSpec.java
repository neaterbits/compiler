package com.neaterbits.compiler.resolver.types;

import com.neaterbits.language.common.types.ScopedName;
import com.neaterbits.language.common.types.TypeName;
import com.neaterbits.language.common.types.TypeVariant;

public final class TypeSpec extends BaseTypeInfo {

	public TypeSpec(ScopedName scopedName, TypeVariant typeVariant) {
		super(scopedName, typeVariant);
	}

	@Override
	public TypeName getTypeName() {
		throw new UnsupportedOperationException();
	}
}
