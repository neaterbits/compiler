package com.neaterbits.compiler.common;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.type.complex.ComplexType;

public class ComplexTypeReference extends ResolvedTypeReference {

	private final ComplexType type;

	public ComplexTypeReference(Context context, ComplexType type) {
		super(context);

		Objects.requireNonNull(type);
		
		this.type = type;
	}

	@Override
	public BaseType getType() {
		return type;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
