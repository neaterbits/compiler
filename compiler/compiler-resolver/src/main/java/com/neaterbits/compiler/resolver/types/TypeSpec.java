package com.neaterbits.compiler.resolver.types;

import com.neaterbits.build.types.ScopedName;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.types.TypeVariant;

public final class TypeSpec extends BaseTypeInfo {

	public TypeSpec(ScopedName scopedName, TypeVariant typeVariant) {
		super(scopedName, typeVariant);
	}

	@Override
	public TypeName getTypeName() {
		throw new UnsupportedOperationException();
	}
}
