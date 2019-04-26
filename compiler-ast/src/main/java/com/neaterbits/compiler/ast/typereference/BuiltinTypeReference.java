package com.neaterbits.compiler.ast.typereference;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.TypeName;

public abstract class BuiltinTypeReference extends ResolvedNamedTypeReference {

	public abstract boolean isScalar();
	
	public BuiltinTypeReference(Context context, TypeName type) {
		super(context, type);
	}
}
