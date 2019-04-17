package com.neaterbits.compiler.ast.typereference;

import com.neaterbits.compiler.ast.type.NamedType;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.TypeName;

public abstract class ResolvedTypeReference extends TypeReference {

	protected ResolvedTypeReference(Context context) {
		super(context);
	}

	@Override
	public TypeName getTypeName() {
		return ((NamedType)getType()).getTypeName();
	}
}
