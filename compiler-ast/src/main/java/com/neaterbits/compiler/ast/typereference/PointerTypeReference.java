package com.neaterbits.compiler.ast.typereference;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.type.NamedType;
import com.neaterbits.compiler.ast.type.PointerType;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.TypeName;

public final class PointerTypeReference extends ResolvedTypeReference {

	private final PointerType pointerType;
	
	public PointerTypeReference(Context context, PointerType pointerType) {
		super(context);
		
		Objects.requireNonNull(pointerType);
		
		this.pointerType = pointerType;
	}

	@Override
	public String getDebugName() {
		return getTypeName().getName() + "_ptr";
	}

	public PointerType getType() {
		return pointerType;
	}
	
	@Override
	public TypeName getTypeName() {
		return ((NamedType)pointerType.getDelegate()).getTypeName();
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onPointerTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
