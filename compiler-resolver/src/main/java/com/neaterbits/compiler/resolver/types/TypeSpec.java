package com.neaterbits.compiler.resolver.types;

import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;

public final class TypeSpec extends BaseTypeInfo {

	public TypeSpec(ScopedName scopedName, TypeVariant typeVariant) {
		super(scopedName, typeVariant);
	}

	@Override
	public TypeName getTypeName() {
		throw new UnsupportedOperationException();
	}
}
