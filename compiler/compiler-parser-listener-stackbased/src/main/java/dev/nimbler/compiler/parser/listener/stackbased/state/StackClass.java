package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.ClassFieldMemberSetter;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.ClassMethodMemberSetter;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.ConstructorMemberSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

public abstract class StackClass<
	COMPLEX_MEMBER_DEFINITION,
	CLASS_FIELD_MEMBER extends COMPLEX_MEMBER_DEFINITION,
	CONSTRUCTOR_MEMBER extends COMPLEX_MEMBER_DEFINITION,
	CLASS_METHOD_MEMBER extends COMPLEX_MEMBER_DEFINITION>

	extends ListStackEntry<COMPLEX_MEMBER_DEFINITION>
		implements
			ClassMethodMemberSetter<CLASS_METHOD_MEMBER>,
			ConstructorMemberSetter<CONSTRUCTOR_MEMBER>,
			ClassFieldMemberSetter<CLASS_FIELD_MEMBER> {

	public StackClass(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
    public void addField(CLASS_FIELD_MEMBER field) {

	    Objects.requireNonNull(field);
	    
	    add(field);
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
