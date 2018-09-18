package com.neaterbits.compiler.common.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.block.MethodName;
import com.neaterbits.compiler.common.ast.list.ASTSingle;
import com.neaterbits.compiler.common.parser.MethodInvocationType;

public final class MethodInvocationExpression extends CallExpression<MethodName> {

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

	public MethodInvocationType getType() {
		return type;
	}

	public TypeReference getClassType() {
		return classType.get();
	}

	public Expression getObject() {
		return object.get();
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onMethodInvocation(this, param);
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {

		if (classType != null) {
			doIterate(classType, recurseMode, visitor);
		}
		
		if (object != null) {
			doIterate(object, recurseMode, visitor);
		}
	}
}
