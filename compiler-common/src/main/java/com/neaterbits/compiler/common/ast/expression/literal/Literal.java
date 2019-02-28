package com.neaterbits.compiler.common.ast.expression.literal;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.type.primitive.ScalarType;

public abstract class Literal extends Primary {

	private final ScalarType type;
	
	public Literal(Context context, ScalarType type) {
		super(context);
		
		Objects.requireNonNull(type);
		
		this.type = type;
	}
	
	@Override
	public final BaseType getType() {
		return type;
	}

	@Override
	public final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
