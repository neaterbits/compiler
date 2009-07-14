package com.neaterbits.compiler.common;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.type.PointerType;

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
	public BaseType getType() {
		return getPointerType();
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
