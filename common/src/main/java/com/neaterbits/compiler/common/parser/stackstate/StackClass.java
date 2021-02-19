package com.neaterbits.compiler.common.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ListStackEntry;
import com.neaterbits.compiler.common.parser.ClassMethodMemberSetter;

public abstract class StackClass extends ListStackEntry<ComplexMemberDefinition> implements ClassMethodMemberSetter {

	public StackClass(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public void addMethod(ClassMethodMember method) {
		
		Objects.requireNonNull(method);
		
		super.add(method);
	}
}
