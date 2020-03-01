package com.neaterbits.compiler.ast.objects.expression.literal;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.type.primitive.ScalarType;
import com.neaterbits.compiler.ast.objects.typereference.ScalarTypeReference;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
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
		return new ScalarTypeReference(getContext(), type.getTypeName());
	}

	@Override
	public final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
