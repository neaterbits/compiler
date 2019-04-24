package com.neaterbits.compiler.ast.typereference;

import com.neaterbits.compiler.ast.type.NamedType;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.TypeName;

public abstract class ResolvedNamedTypeReference extends ResolvedTypeReference {

	public ResolvedNamedTypeReference(Context context) {
		super(context);
	}

	public abstract NamedType getNamedType();

	@Override
	public final TypeName getTypeName() {
		return getNamedType().getTypeName();
	}

	@Override
	public String getDebugName() {
		return getTypeName().getName();
	}
}
