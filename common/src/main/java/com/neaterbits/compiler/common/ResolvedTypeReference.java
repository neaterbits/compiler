package com.neaterbits.compiler.common;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.type.BaseType;

public final class ResolvedTypeReference extends TypeReference {

	private final BaseType type;

	public ResolvedTypeReference(Context context, BaseType type) {
		super(context);

		Objects.requireNonNull(type);
		
		this.type = type;
	}

	@Override
	public BaseType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "ResolvedTypeReference [type=" + type + "]";
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		
	}
}
