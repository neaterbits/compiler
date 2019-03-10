package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.ast.parser.ClassMethodMemberSetter;
import com.neaterbits.compiler.ast.parser.ConstructorMemberSetter;
import com.neaterbits.compiler.ast.parser.ListStackEntry;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.ast.typedefinition.ConstructorMember;

public abstract class StackClass extends ListStackEntry<ComplexMemberDefinition>
		implements ClassMethodMemberSetter, ConstructorMemberSetter {

	public StackClass(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public void addMethod(ClassMethodMember method) {
		
		Objects.requireNonNull(method);
		
		add(method);
	}

	@Override
	public void addConstructorMember(ConstructorMember constructor) {

		Objects.requireNonNull(constructor);
		
		add(constructor);
	}
}
