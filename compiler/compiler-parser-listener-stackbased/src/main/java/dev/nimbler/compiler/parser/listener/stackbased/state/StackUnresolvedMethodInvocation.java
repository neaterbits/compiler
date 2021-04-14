package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.parser.listener.stackbased.state.setters.PrimarySetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackUnresolvedMethodInvocation<EXPRESSION, PRIMARY extends EXPRESSION, TYPE_REFERENCE>
	extends CallStackEntry<EXPRESSION>
	implements PrimarySetter<PRIMARY> {

	// private final MethodInvocationType type;
	// private final TYPE_REFERENCE classType;
	private PRIMARY object;
	
	public StackUnresolvedMethodInvocation(
	        ParseLogger parseLogger,
	        // MethodInvocationType type,
	        // TYPE_REFERENCE classType,
	        String methodName,
	        Context methodNameContext) {
	    
		super(parseLogger);

		// Objects.requireNonNull(type);
		Objects.requireNonNull(methodName);
		Objects.requireNonNull(methodNameContext);
		
		setName(methodName, methodNameContext);

		// this.classType = classType;
		// this.type = type;
	}

	/*
    @Deprecated
	public MethodInvocationType getType() {
		return type;
	}

	@Deprecated
	public TYPE_REFERENCE getClassType() {
		return classType;
	}

    @Deprecated
	public EXPRESSION getObject() {
		return object;
	}
	*/

    @Deprecated
	private void setExpression(PRIMARY primary) {
		
		Objects.requireNonNull(primary);
		
		if (this.object != null) {
			throw new IllegalStateException("Object primary already set");
		}

		this.object = primary;
	}
	
	@Override
	public void addPrimary(PRIMARY primary) {
		
	    /*
		if (type != MethodInvocationType.PRIMARY) {
			throw new IllegalStateException("Expected " + MethodInvocationType.PRIMARY);
		}
		*/

		setExpression(primary);
	}
}

