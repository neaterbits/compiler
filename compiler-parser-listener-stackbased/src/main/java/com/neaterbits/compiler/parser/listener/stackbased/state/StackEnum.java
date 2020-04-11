package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.ClassMethodMemberSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.ClassModifierSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.ConstructorMemberSetter;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackEnum<
			COMPLEX_MEMBER_DEFINITION,
			TYPE_REFERENCE,
			CLASS_MODIFIER_HOLDER,
			CONSTRUCTOR_MEMBER extends COMPLEX_MEMBER_DEFINITION,
			CLASS_METHOD_MEMBER extends COMPLEX_MEMBER_DEFINITION,
			ENUM_CONSTANT_DEFINITION
			>

	extends ListStackEntry<COMPLEX_MEMBER_DEFINITION>
	implements
			ClassModifierSetter<CLASS_MODIFIER_HOLDER>,
			ConstructorMemberSetter<CONSTRUCTOR_MEMBER>,
			ClassMethodMemberSetter<CLASS_METHOD_MEMBER> {

	private final String enumKeyword;
	private final Context enumKeywordContext;
	private final String name;
	private final Context nameContext;
	private final List<CLASS_MODIFIER_HOLDER> modifiers;
	private final List<TYPE_REFERENCE> implementedInterfaces;
	private final List<ENUM_CONSTANT_DEFINITION> constants;
	
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

	public List<CLASS_MODIFIER_HOLDER> getModifiers() {
		return modifiers;
	}
	
	@Override
	public void addClassModifier(CLASS_MODIFIER_HOLDER modifier) {

		Objects.requireNonNull(modifier);

		modifiers.add(modifier);
	}

	public List<TYPE_REFERENCE> getImplementedInterfaces() {
		return implementedInterfaces;
	}

	public void addImplementedInterface(TYPE_REFERENCE implementedInterface) {
		Objects.requireNonNull(implementedInterface);

		implementedInterfaces.add(implementedInterface);
	}

	@Override
	public void addConstructorMember(CONSTRUCTOR_MEMBER constructor) {

		Objects.requireNonNull(constructor);
		
		add(constructor);
		
	}

	@Override
	public void addMethod(CLASS_METHOD_MEMBER method) {

		Objects.requireNonNull(method);
		
		add(method);
	}

	public List<ENUM_CONSTANT_DEFINITION> getConstants() {
		return constants;
	}

	public void addConstant(ENUM_CONSTANT_DEFINITION constant) {
		Objects.requireNonNull(constant);

		constants.add(constant);
	}

}