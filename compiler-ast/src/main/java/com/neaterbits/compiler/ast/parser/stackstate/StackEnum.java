package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.parser.ClassMethodMemberSetter;
import com.neaterbits.compiler.ast.parser.ClassModifierSetter;
import com.neaterbits.compiler.ast.parser.ConstructorMemberSetter;
import com.neaterbits.compiler.ast.parser.ListStackEntry;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.ast.typedefinition.ClassModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.ast.typedefinition.ConstructorMember;
import com.neaterbits.compiler.ast.typedefinition.EnumConstantDefinition;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackEnum extends ListStackEntry<ComplexMemberDefinition> implements ClassModifierSetter, ConstructorMemberSetter, ClassMethodMemberSetter {

	private final String enumKeyword;
	private final Context enumKeywordContext;
	private final String name;
	private final Context nameContext;
	private final List<ClassModifierHolder> modifiers;
	private final List<TypeReference> implementedInterfaces;
	private final List<EnumConstantDefinition> constants;
	
	public StackEnum(ParseLogger parseLogger, String enumKeyword, Context enumKeywordContext, String name, Context nameContext) {
		super(parseLogger);

		Objects.requireNonNull(enumKeyword);
		Objects.requireNonNull(enumKeywordContext);
		Objects.requireNonNull(name);
		Objects.requireNonNull(nameContext);

		this.enumKeyword = enumKeyword;
		this.enumKeywordContext = enumKeywordContext;
		this.name = name;
		this.nameContext = nameContext;
		this.modifiers = new ArrayList<>();
		this.implementedInterfaces = new ArrayList<>();
		this.constants = new ArrayList<>();
	}

	public String getEnumKeyword() {
		return enumKeyword;
	}

	public Context getEnumKeywordContext() {
		return enumKeywordContext;
	}

	public String getName() {
		return name;
	}

	public Context getNameContext() {
		return nameContext;
	}

	public List<ClassModifierHolder> getModifiers() {
		return modifiers;
	}
	
	@Override
	public void addClassModifier(ClassModifierHolder modifier) {

		Objects.requireNonNull(modifier);

		modifiers.add(modifier);
	}

	public List<TypeReference> getImplementedInterfaces() {
		return implementedInterfaces;
	}

	public void addImplementedInterface(TypeReference implementedInterface) {
		Objects.requireNonNull(implementedInterface);

		implementedInterfaces.add(implementedInterface);
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
