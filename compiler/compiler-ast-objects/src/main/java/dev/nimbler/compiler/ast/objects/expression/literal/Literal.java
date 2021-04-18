package dev.nimbler.compiler.ast.objects.expression.literal;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.type.primitive.ScalarType;
import dev.nimbler.compiler.ast.objects.typereference.ScalarTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.variables.ResolvedPrimary;

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
