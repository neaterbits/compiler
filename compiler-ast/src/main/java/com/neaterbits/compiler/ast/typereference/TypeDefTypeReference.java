package com.neaterbits.compiler.ast.typereference;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.type.TypeDefType;
import com.neaterbits.compiler.util.Context;

public final class TypeDefTypeReference extends ResolvedNamedTypeReference {

	private final TypeDefType type;

	public TypeDefTypeReference(Context context, TypeDefType type) {
		super(context);

		Objects.requireNonNull(type);

		this.type = type;
	}

	@Override
	public TypeDefType getNamedType() {
		return type;
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onTypeDefTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
