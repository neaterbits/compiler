package com.neaterbits.compiler.ast.objects.typereference;

import com.neaterbits.language.common.types.TypeName;
import com.neaterbits.util.parse.context.Context;

public abstract class BuiltinTypeReference extends ResolvedNamedTypeReference {

	public abstract boolean isScalar();

	public BuiltinTypeReference(Context context, int typeNo, TypeName type) {
		super(context, typeNo, type);
	}

    protected BuiltinTypeReference(ResolvedNamedTypeReference other) {
        super(other);
    }
}
