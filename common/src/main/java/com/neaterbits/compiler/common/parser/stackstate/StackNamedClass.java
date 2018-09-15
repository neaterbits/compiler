package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.typedefinition.ClassModifier;

public final class StackNamedClass extends StackClass {

	private final String name;
	private final List<ClassModifier> modifiers;
	
	public StackNamedClass(String name) {
		Objects.requireNonNull(name);

		this.name = name;
		this.modifiers = new ArrayList<>();
	}
	
	public void addModifier(ClassModifier modifier) {
		Objects.requireNonNull(modifiers);

		modifiers.add(modifier);
	}
	
	public String getName() {
		return name;
	}

	public List<ClassModifier> getModifiers() {
		return modifiers;
	}
}
