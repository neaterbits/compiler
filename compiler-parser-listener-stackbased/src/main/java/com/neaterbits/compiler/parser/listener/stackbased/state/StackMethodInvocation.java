package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.setters.PrimarySetter;
import com.neaterbits.compiler.types.method.MethodInvocationType;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackMethodInvocation<EXPRESSION, PRIMARY extends EXPRESSION, TYPE_REFERENCE>
	extends CallStackEntry<EXPRESSION>
	implements PrimarySetter<PRIMARY> {

	private final MethodInvocationType type;
	private final TYPE_REFERENCE classType;
	private PRIMARY object;
	
	public StackMethodInvocation(ParseLogger parseLogger, MethodInvocationType type, TYPE_REFERENCE classType, String methodName, Context methodNameContext) {
		super(parseLogger);

		Objects.requireNonNull(type);
		Objects.requireNonNull(methodName);
		Objects.requireNonNull(methodNameContext);
		
		setName(methodName, methodNameContext);

		this.classType = classType;
		this.type = type;
	}

	public MethodInvocationType getType() {
		return type;
	}

	public TYPE_REFERENCE getClassType() {
		return classType;
	}

	public EXPRESSION getObject() {
		return object;
	}

	private void setExpression(PRIMARY primary) {
		
		Objects.requireNonNull(primary);
		
		if (this.object != null) {
			throw new IllegalStateException("Object primary already set");
		}

		this.object = primary;
	}
	
	@Override
	public void addPrimary(PRIMARY primary) {
		
		if (type != MethodInvocationType.PRIMARY) {
			throw new IllegalStateException("Expected " + MethodInvocationType.PRIMARY);
		}

		setExpression(primary);
	}
}

