package com.neaterbits.compiler.ast.typereference;

import com.neaterbits.compiler.util.Context;

public abstract class ResolvedTypeReference extends TypeReference {

	protected ResolvedTypeReference(Context context) {
		super(context);
	}
}
