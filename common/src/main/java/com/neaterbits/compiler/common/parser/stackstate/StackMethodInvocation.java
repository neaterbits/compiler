package com.neaterbits.compiler.common.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.block.MethodName;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ExpressionSetter;
import com.neaterbits.compiler.common.parser.MethodInvocationType;

public final class StackMethodInvocation extends CallStackEntry<MethodName> implements ExpressionSetter {

	private final MethodInvocationType type;
	private final TypeReference classType;
	private Expression object;
	
	public StackMethodInvocation(ParseLogger parseLogger, MethodInvocationType type, TypeReference classType, String methodName) {
		super(parseLogger);

		Objects.requireNonNull(type);
		Objects.requireNonNull(methodName);

		setName(new MethodName(methodName));

		this.classType = classType;
		this.type = type;
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

	private void setExpression(Expression expression) {
		
		Objects.requireNonNull(expression);
		
		if (this.object != null) {
			throw new IllegalStateException("Object expression already set");
		}

		this.object = expression;
	}
	
	@Override
	public void addExpression(Expression expression) {
		
		if (type != MethodInvocationType.EXPRESSION) {
			throw new IllegalStateException("Expected " + MethodInvocationType.EXPRESSION);
		}

		setExpression(expression);
	}
}

