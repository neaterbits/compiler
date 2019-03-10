package com.neaterbits.compiler.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.typedefinition.ConstructorName;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

public final class ClassInstanceCreationExpression extends Call<ConstructorName> {

	private final ASTSingle<TypeReference> type;

	public ClassInstanceCreationExpression(Context context, TypeReference type, ConstructorName name, ParameterList parameters) {
		super(context, name, parameters);

		Objects.requireNonNull(type);
		Objects.requireNonNull(parameters);

		this.type = makeSingle(type);
	}

	public TypeReference getTypeReference() {
		return type.get();
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onClassInstanceCreation(this, param);
	}
}
