package com.neaterbits.compiler.common.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.ConstructorMember;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ListStackEntry;
import com.neaterbits.compiler.common.parser.ClassMethodMemberSetter;
import com.neaterbits.compiler.common.parser.ConstructorMemberSetter;

public abstract class StackClass extends ListStackEntry<ComplexMemberDefinition>
		implements ClassMethodMemberSetter, ConstructorMemberSetter {

	public StackClass(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public void addMethod(ClassMethodMember method) {
		
		Objects.requireNonNull(method);
		
		super.add(method);
	}

	@Override
	public void addConstructorMember(ConstructorMember constructor) {

		Objects.requireNonNull(constructor);
		
		add(constructor);
		
	}
}
