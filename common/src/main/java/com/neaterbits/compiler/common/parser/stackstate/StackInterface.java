package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceMethodMember;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceModifierHolder;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.InterfaceMethodMemberSetter;
import com.neaterbits.compiler.common.parser.ListStackEntry;

public final class StackInterface extends ListStackEntry<ComplexMemberDefinition> implements InterfaceMethodMemberSetter {

	private final String name;
	private final List<InterfaceModifierHolder> modifiers;
	private final List<TypeReference> extendedInterfaces;
	
	public StackInterface(ParseLogger parseLogger, String name) {
		super(parseLogger);

		Objects.requireNonNull(name);

		this.name = name;
		this.modifiers = new ArrayList<>();
		this.extendedInterfaces = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}

	public List<InterfaceModifierHolder> getModifiers() {
		return modifiers;
	}

	public void addModifier(InterfaceModifierHolder modifier) {
		modifiers.add(modifier);
	}

	@Override
	public void addMethod(InterfaceMethodMember method) {
		add(method);
	}
	
	public List<TypeReference> getExtendedInterfaces() {
		return extendedInterfaces;
	}

	public void addExtendedInterface(TypeReference extendedInterface) {
		Objects.requireNonNull(extendedInterface);

		extendedInterfaces.add(extendedInterface);
	}
}
