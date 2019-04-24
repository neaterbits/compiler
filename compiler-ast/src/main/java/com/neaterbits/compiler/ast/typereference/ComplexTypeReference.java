package com.neaterbits.compiler.ast.typereference;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.type.NamedType;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.util.Context;

public final class ComplexTypeReference extends ResolvedNamedTypeReference {

	private final ComplexType<?, ?, ?> type;

	public ComplexTypeReference(Context context, ComplexType<?, ?, ?> type) {
		super(context);

		Objects.requireNonNull(type);
		
		this.type = type;
	}

	@Override
	public String getDebugName() {
		return ((NamedType)type).getName().getName();
	}

	@Override
	public ComplexType<?, ?, ?> getNamedType() {
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
