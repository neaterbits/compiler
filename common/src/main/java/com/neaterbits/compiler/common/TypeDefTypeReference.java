package com.neaterbits.compiler.common;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.type.TypeDefType;

public final class TypeDefTypeReference extends ResolvedTypeReference {

	private final TypeDefType type;

	public TypeDefTypeReference(Context context, TypeDefType type) {
		super(context);

		Objects.requireNonNull(type);

		this.type = type;
	}

	@Override
	public BaseType getType() {
		return type;
	}

	@Override
	public String getDebugName() {
		return type.getName().getName();
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onTypeDefTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
