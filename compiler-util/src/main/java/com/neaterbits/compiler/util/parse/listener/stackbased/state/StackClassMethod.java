package com.neaterbits.compiler.util.parse.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackClassMethod<STATEMENT, PARAMETER, TYPE_REFERENCE, MODIFIER_HOLDER>
	extends CallableStackEntry<STATEMENT, PARAMETER, TYPE_REFERENCE> {

	private final List<MODIFIER_HOLDER> modifiers;
	
	public StackClassMethod(ParseLogger parseLogger) {
		super(parseLogger);
		
		this.modifiers = new ArrayList<>();
	}

	public StackClassMethod(ParseLogger parseLogger, String name, Context nameContext) {
		super(parseLogger, name, nameContext);

		this.modifiers = new ArrayList<>();
	}
	
	public void addModifier(MODIFIER_HOLDER modifier) {
		Objects.requireNonNull(modifier);

		modifiers.add(modifier);
	}

	public List<MODIFIER_HOLDER> getModifiers() {
		return modifiers;
	}
}
