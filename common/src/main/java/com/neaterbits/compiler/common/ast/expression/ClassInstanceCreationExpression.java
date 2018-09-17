package com.neaterbits.compiler.common.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.typedefinition.ConstructorName;

public final class ClassInstanceCreationExpression extends CallExpression<ConstructorName> {

	private final TypeReference type;

	public ClassInstanceCreationExpression(Context context, TypeReference type, ConstructorName name, ParameterList parameters) {
		super(context, name, parameters);

		Objects.requireNonNull(type);
		Objects.requireNonNull(parameters);

		this.type = type;
	}

	public TypeReference getType() {
		return type;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onClassInstanceCreation(this, param);
	}
}
