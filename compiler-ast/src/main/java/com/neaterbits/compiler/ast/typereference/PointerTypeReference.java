package com.neaterbits.compiler.ast.typereference;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.ast.type.NamedType;
import com.neaterbits.compiler.ast.type.PointerType;
import com.neaterbits.compiler.util.Context;

public final class PointerTypeReference extends ResolvedTypeReference {

	private final PointerType pointerType;
	
	public PointerTypeReference(Context context, PointerType pointerType) {
		super(context);
		
		Objects.requireNonNull(pointerType);
		
		this.pointerType = pointerType;
	}

	public PointerType getPointerType() {
		return pointerType;
	}

	@Override
	public String getDebugName() {
		return ((NamedType)pointerType.getDelegate()).getName().getName() + "_ptr";
	}

	@Override
	public BaseType getType() {
		return getPointerType();
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onPointerTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}