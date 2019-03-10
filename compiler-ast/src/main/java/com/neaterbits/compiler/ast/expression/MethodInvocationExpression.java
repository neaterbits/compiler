package com.neaterbits.compiler.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.block.MethodName;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.parser.MethodInvocationType;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

public final class MethodInvocationExpression extends Call<MethodName> {

	private final MethodInvocationType type;
	private final ASTSingle<TypeReference> classType;
	private final ASTSingle<Expression> object;

	public MethodInvocationExpression(Context context, MethodInvocationType type, TypeReference classType, Expression object, MethodName callable, ParameterList parameters) {
		super(context, callable, parameters);

		Objects.requireNonNull(type);

		if (type.requiresClassName()) {
			Objects.requireNonNull(classType);
		}

		this.type = type;
		this.classType = classType != null ? makeSingle(classType) : null;
		this.object = object != null ? makeSingle(object) : null;
	}

	public MethodInvocationType getInvocationType() {
		return type;
	}

	public TypeReference getClassType() {
		return classType.get();
	}

	public Expression getObject() {
		return object != null ? object.get() : null;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onMethodInvocation(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		if (classType != null) {
			doIterate(classType, recurseMode, iterator);
		}
		
		if (object != null) {
			doIterate(object, recurseMode, iterator);
		}

		super.doRecurse(recurseMode, iterator);
	}
}
