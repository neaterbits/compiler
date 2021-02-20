package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.typedefinition.ClassModifierHolder;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ClassModifierSetter;

public final class StackNamedClass extends StackClass implements ClassModifierSetter {

	private final String name;
	private final List<ClassModifierHolder> modifiers;
	
	public StackNamedClass(ParseLogger parseLogger, String name) {
		super(parseLogger);

		Objects.requireNonNull(name);

		this.name = name;
		this.modifiers = new ArrayList<>();
	}
	
	@Override
	public void addClassModifier(ClassModifierHolder modifier) {
		Objects.requireNonNull(modifiers);

		modifiers.add(modifier);
	}
	
	public String getName() {
		return name;
	}

	public List<ClassModifierHolder> getModifiers() {
		return modifiers;
	}
}
