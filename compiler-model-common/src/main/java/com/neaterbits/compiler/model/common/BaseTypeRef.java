package com.neaterbits.compiler.model.common;

import java.util.Objects;

import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;

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
