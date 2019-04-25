package com.neaterbits.compiler.ast.typereference;

import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.TypeName;

public abstract class ResolvedNamedTypeReference extends ResolvedTypeReference {

	private final TypeName typeName;
	
	public ResolvedNamedTypeReference(Context context, TypeName typeName) {
		super(context);
		
		Objects.requireNonNull(typeName);
		
		this.typeName = typeName;
	}


	@Override
	public final TypeName getTypeName() {
		return typeName;
	}

	@Override
	public String getDebugName() {
		return typeName.getName();
	}
}
