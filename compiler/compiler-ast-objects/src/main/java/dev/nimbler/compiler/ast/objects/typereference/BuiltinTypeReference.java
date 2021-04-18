package dev.nimbler.compiler.ast.objects.typereference;

import org.jutils.parse.context.Context;

import dev.nimbler.language.common.types.TypeName;

public abstract class BuiltinTypeReference extends ResolvedNamedTypeReference {

	public abstract boolean isScalar();

	public BuiltinTypeReference(Context context, int typeNo, TypeName type) {
		super(context, typeNo, type);
	}

    protected BuiltinTypeReference(ResolvedNamedTypeReference other) {
        super(other);
    }
}
