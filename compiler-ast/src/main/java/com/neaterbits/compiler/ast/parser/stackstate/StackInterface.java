package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.parser.InterfaceMethodMemberSetter;
import com.neaterbits.compiler.ast.parser.ListStackEntry;
import com.neaterbits.compiler.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.ast.typedefinition.InterfaceMethodMember;
import com.neaterbits.compiler.ast.typedefinition.InterfaceModifierHolder;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackInterface extends ListStackEntry<ComplexMemberDefinition> implements InterfaceMethodMemberSetter {

	private final String interfaceKeyword;
	private final Context interfaceKeywordContext;
	private final String name;
	private final Context nameContext;
	private final List<InterfaceModifierHolder> modifiers;
	private final List<TypeReference> extendedInterfaces;
	
	public StackInterface(ParseLogger parseLogger, String interfaceKeyword, Context interfaceKeywordContext, String name, Context nameContext) {
		super(parseLogger);

		Objects.requireNonNull(interfaceKeyword);
		Objects.requireNonNull(interfaceKeywordContext);
		Objects.requireNonNull(name);
		Objects.requireNonNull(nameContext);

		this.interfaceKeyword = interfaceKeyword;
		this.interfaceKeywordContext = interfaceKeywordContext;
		this.name = name;
		this.nameContext = nameContext;
		this.modifiers = new ArrayList<>();
		this.extendedInterfaces = new ArrayList<>();
	}
	
	public String getInterfaceKeyword() {
		return interfaceKeyword;
	}

	public Context getInterfaceKeywordContext() {
		return interfaceKeywordContext;
	}

	public String getName() {
		return name;
	}

	public Context getNameContext() {
		return nameContext;
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
