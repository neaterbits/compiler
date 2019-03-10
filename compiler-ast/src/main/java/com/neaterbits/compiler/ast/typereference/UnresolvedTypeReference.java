package com.neaterbits.compiler.ast.typereference;

import com.neaterbits.compiler.util.Context;

public abstract class UnresolvedTypeReference extends TypeReference {

	protected UnresolvedTypeReference(Context context) {
		super(context);
	}
	
}
