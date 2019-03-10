package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.ast.block.MethodName;
import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.expression.literal.Primary;
import com.neaterbits.compiler.ast.parser.MethodInvocationType;
import com.neaterbits.compiler.ast.parser.PrimarySetter;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackMethodInvocation extends CallStackEntry<MethodName> implements PrimarySetter {

	private final MethodInvocationType type;
	private final TypeReference classType;
	private Primary object;
	
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

	private void setExpression(Primary primary) {
		
		Objects.requireNonNull(primary);
		
		if (this.object != null) {
			throw new IllegalStateException("Object primary already set");
		}

		this.object = primary;
	}
	
	@Override
	public void addPrimary(Primary primary) {
		
		if (type != MethodInvocationType.PRIMARY) {
			throw new IllegalStateException("Expected " + MethodInvocationType.PRIMARY);
		}

		setExpression(primary);
	}
}

