package com.neaterbits.compiler.util.parse.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackConstructor<STATEMENT, PARAMETER, TYPE_REFERENCE, CONSTRUCTOR_MODIFIER_HOLDER>
	extends CallableStackEntry<STATEMENT, PARAMETER, TYPE_REFERENCE> {

	private final List<CONSTRUCTOR_MODIFIER_HOLDER> modifiers;
	
	public StackConstructor(ParseLogger parseLogger, String name, Context nameContext) {
		super(parseLogger, name, nameContext);

		this.modifiers = new ArrayList<>();
	}

	public StackConstructor(ParseLogger parseLogger) {
		super(parseLogger);
		
		this.modifiers = new ArrayList<>();
	}

	public List<CONSTRUCTOR_MODIFIER_HOLDER> getModifiers() {
		return modifiers;
	}
	
	public void addModifier(CONSTRUCTOR_MODIFIER_HOLDER modifier) {
		Objects.requireNonNull(modifier);
		
		modifiers.add(modifier);
	}
}
