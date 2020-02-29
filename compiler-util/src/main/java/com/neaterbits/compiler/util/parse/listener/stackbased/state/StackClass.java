package com.neaterbits.compiler.util.parse.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.base.ListStackEntry;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.setters.ClassMethodMemberSetter;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.setters.ConstructorMemberSetter;

public abstract class StackClass<
	COMPLEX_MEMBER_DEFINITION,
	CONSTRUCTOR_MEMBER extends COMPLEX_MEMBER_DEFINITION,
	CLASS_METHOD_MEMBER extends COMPLEX_MEMBER_DEFINITION>

	extends ListStackEntry<COMPLEX_MEMBER_DEFINITION>
		implements
			ClassMethodMemberSetter<CLASS_METHOD_MEMBER>,
			ConstructorMemberSetter<CONSTRUCTOR_MEMBER> {

	public StackClass(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public void addMethod(CLASS_METHOD_MEMBER method) {
		
		Objects.requireNonNull(method);
		
		add(method);
	}

	@Override
	public void addConstructorMember(CONSTRUCTOR_MEMBER constructor) {

		Objects.requireNonNull(constructor);
		
		add(constructor);
	}
}
