package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.common.ast.typedefinition.ClassModifierHolder;
import com.neaterbits.compiler.common.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.ConstructorMember;
import com.neaterbits.compiler.common.ast.typedefinition.EnumConstantDefinition;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ClassMethodMemberSetter;
import com.neaterbits.compiler.common.parser.ClassModifierSetter;
import com.neaterbits.compiler.common.parser.ConstructorMemberSetter;
import com.neaterbits.compiler.common.parser.ListStackEntry;

public final class StackEnum extends ListStackEntry<ComplexMemberDefinition> implements ClassModifierSetter, ConstructorMemberSetter, ClassMethodMemberSetter {

	private final String name;
	private final List<ClassModifierHolder> modifiers;
	private final List<EnumConstantDefinition> constants;
	
	public StackEnum(ParseLogger parseLogger, String name) {
		super(parseLogger);

		Objects.requireNonNull(name);

		this.name = name;
		this.modifiers = new ArrayList<>();
		this.constants = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public List<ClassModifierHolder> getModifiers() {
		return modifiers;
	}
	
	@Override
	public void addClassModifier(ClassModifierHolder modifier) {

		Objects.requireNonNull(modifier);

		modifiers.add(modifier);
	}
	
	@Override
	public void addConstructorMember(ConstructorMember constructor) {

		Objects.requireNonNull(constructor);
		
		add(constructor);
		
	}

	@Override
	public void addMethod(ClassMethodMember method) {

		Objects.requireNonNull(method);
		
		add(method);
	}

	public List<EnumConstantDefinition> getConstants() {
		return constants;
	}

	public void addConstant(EnumConstantDefinition constant) {
		Objects.requireNonNull(constant);

		constants.add(constant);
	}
}
