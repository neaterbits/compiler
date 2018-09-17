package com.neaterbits.compiler.common.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.block.MethodName;
import com.neaterbits.compiler.common.parser.MethodInvocationType;

public final class MethodInvocationExpression extends CallExpression<MethodName> {

	private final MethodInvocationType type;
	private final TypeReference classType;
	private final Expression object;

	public MethodInvocationExpression(Context context, MethodInvocationType type, TypeReference classType, Expression object, MethodName callable, ParameterList parameters) {
		super(context, callable, parameters);

		Objects.requireNonNull(type);

		if (type.requiresClassName()) {
			Objects.requireNonNull(classType);
		}

		this.type = type;
		this.classType = classType;
		this.object = object;
	}

	public MethodInvocationType getType() {
		return type;
	}

	public TypeReference getClassType() {
		return classType;
	}

	public Expression getObject() {
		return object;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onMethodInvocation(this, param);
	}
}
