package dev.nimbler.compiler.resolver.types;

import dev.nimbler.language.common.types.ScopedName;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.types.TypeVariant;

public final class TypeSpec extends BaseTypeInfo {

	public TypeSpec(ScopedName scopedName, TypeVariant typeVariant) {
		super(scopedName, typeVariant);
	}

	@Override
	public TypeName getTypeName() {
		throw new UnsupportedOperationException();
	}
}
