package com.neaterbits.compiler.common;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.type.FunctionPointerType;

public final class FunctionPointerTypeReference extends ResolvedTypeReference {

	private final FunctionPointerType type;
	
	public FunctionPointerTypeReference(Context context, FunctionPointerType type) {
		super(context);

		Objects.requireNonNull(type);
		
		this.type = type;
	}

	public FunctionPointerType getType() {
		return type;
	}

	@Override
	public String getDebugName() {
		return type.toString();
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onFunctionPointerTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
