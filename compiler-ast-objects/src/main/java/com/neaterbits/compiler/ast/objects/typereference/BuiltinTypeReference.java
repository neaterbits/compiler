package com.neaterbits.compiler.ast.objects.typereference;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.TypeName;

public abstract class BuiltinTypeReference extends ResolvedNamedTypeReference {

	public abstract boolean isScalar();

	public BuiltinTypeReference(Context context, int typeNo, TypeName type) {
		super(context, typeNo, type);
	}
}
