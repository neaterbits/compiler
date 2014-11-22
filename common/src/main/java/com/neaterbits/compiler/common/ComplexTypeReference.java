package com.neaterbits.compiler.common;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.type.NamedType;
import com.neaterbits.compiler.common.ast.type.complex.ComplexType;

public final class ComplexTypeReference extends ResolvedTypeReference {

	private final ComplexType<?> type;

	public ComplexTypeReference(Context context, ComplexType<?> type) {
		super(context);

		Objects.requireNonNull(type);
		
		this.type = type;
	}

	@Override
	public String getDebugName() {
		return ((NamedType)type).getName().getName();
	}

	@Override
	public BaseType getType() {
		return type;
	}
	
	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onComplexTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
