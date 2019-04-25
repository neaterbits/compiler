package com.neaterbits.compiler.ast.expression.literal;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.type.primitive.ScalarType;
import com.neaterbits.compiler.ast.typereference.BuiltinTypeReference;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

public abstract class Literal extends Primary {

	private final ScalarType type;
	
	public Literal(Context context, ScalarType type) {
		super(context);
		
		Objects.requireNonNull(type);
		
		this.type = type;
	}
	
	@Override
	public final TypeReference getType() {
		return new BuiltinTypeReference(getContext(), type.getTypeName(), true);
	}

	@Override
	public final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
