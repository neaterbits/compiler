package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.parser.ClassModifierSetter;
import com.neaterbits.compiler.ast.typedefinition.ClassModifierHolder;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackNamedClass extends StackClass implements ClassModifierSetter {

	private final String name;
	private final Context nameContext;
	private final List<ClassModifierHolder> modifiers;
	
	private final List<TypeReference> extendedClasses;
	private final List<TypeReference> implementedInterfaces;
	
	public StackNamedClass(ParseLogger parseLogger, String name, Context nameContext) {
		super(parseLogger);

		Objects.requireNonNull(name);
		Objects.requireNonNull(nameContext);
		
		this.name = name;
		this.nameContext = nameContext;
		this.modifiers = new ArrayList<>();
		
		this.extendedClasses = new ArrayList<>();
		this.implementedInterfaces = new ArrayList<>();
	}
	
	@Override
	public void addClassModifier(ClassModifierHolder modifier) {
		Objects.requireNonNull(modifiers);

		modifiers.add(modifier);
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

	public List<TypeReference> getExtendedClasses() {
		return extendedClasses;
	}

	public List<TypeReference> getImplementedInterfaces() {
		return implementedInterfaces;
	}

	public void addExtendedClass(TypeReference extendedClass) {
		Objects.requireNonNull(extendedClass);

		extendedClasses.add(extendedClass);
	}

	public void addImplementedInterface(TypeReference implementedInterface) {
		Objects.requireNonNull(implementedInterface);

		implementedInterfaces.add(implementedInterface);
	}
}
