package com.neaterbits.compiler.model.common;

import java.util.Objects;

import com.neaterbits.language.common.types.ScopedName;
import com.neaterbits.language.common.types.TypeName;

public abstract class BaseTypeRef {

	private final TypeName typeName;

	public BaseTypeRef(TypeName typeName) {

		Objects.requireNonNull(typeName);
		
		this.typeName = typeName;
	}

	public final TypeName getTypeName() {
		return typeName;
	}

	public final String getNameString() {
		return typeName.getName();
	}
	
	public final ScopedName toScopedName() {
		return typeName.toScopedName();
	}
}
