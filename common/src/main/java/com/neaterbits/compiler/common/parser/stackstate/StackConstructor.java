package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.typedefinition.ConstructorModifierHolder;
import com.neaterbits.compiler.common.log.ParseLogger;

public final class StackConstructor extends CallableStackEntry {

	private final List<ConstructorModifierHolder> modifiers;
	
	public StackConstructor(ParseLogger parseLogger, String name) {
		super(parseLogger, name);

		this.modifiers = new ArrayList<>();
	}

	public StackConstructor(ParseLogger parseLogger) {
		super(parseLogger);
		
		this.modifiers = new ArrayList<>();
	}

	public List<ConstructorModifierHolder> getModifiers() {
		return modifiers;
	}
	
	public void addModifier(ConstructorModifierHolder modifier) {
		Objects.requireNonNull(modifier);
		
		modifiers.add(modifier);
	}
}
