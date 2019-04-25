package com.neaterbits.compiler.ast.typereference;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.TypeName;

public final class TypeDefTypeReference extends ResolvedNamedTypeReference {

	private final TypeReference aliasedType;

	public TypeDefTypeReference(Context context, TypeName type, TypeReference aliasedType) {
		super(context, type);

		Objects.requireNonNull(type);
		Objects.requireNonNull(aliasedType);

		this.aliasedType = aliasedType;
	}

	public TypeReference getAliasedType() {
		return aliasedType;
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onTypeDefTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
