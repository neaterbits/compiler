package com.neaterbits.compiler.ast.objects.expression.literal;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.type.primitive.ScalarType;
import com.neaterbits.compiler.ast.objects.typereference.ScalarTypeReference;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.ast.objects.variables.ResolvedPrimary;
import com.neaterbits.util.parse.context.Context;

public abstract class Literal extends ResolvedPrimary {

	private final ScalarType type;
    private final int typeNo;

	public Literal(Context context, ScalarType type, int typeNo) {
		super(context);

		Objects.requireNonNull(type);

		this.type = type;
		this.typeNo = typeNo;
	}

	@Override
	public final TypeReference getType() {
		return new ScalarTypeReference(getContext(), typeNo, type.getTypeName());
	}

	@Override
	public final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

	}
}
