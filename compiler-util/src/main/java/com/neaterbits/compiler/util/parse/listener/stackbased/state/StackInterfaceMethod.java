package com.neaterbits.compiler.util.parse.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackInterfaceMethod<
		STATEMENT,
		PARAMETER,
		TYPE_REFERENCE,
		INTERFACE_METHOD_MODIFIER_HOLDER>

	extends CallableStackEntry<STATEMENT, PARAMETER, TYPE_REFERENCE> {

	private final List<INTERFACE_METHOD_MODIFIER_HOLDER> modifiers;

	public StackInterfaceMethod(ParseLogger parseLogger, String name, Context nameContext) {
		super(parseLogger, name, nameContext);

		this.modifiers = new ArrayList<>();
	}

	public StackInterfaceMethod(ParseLogger parseLogger) {
		super(parseLogger);
		
		this.modifiers = new ArrayList<>();
	}
	
	public void addModifier(INTERFACE_METHOD_MODIFIER_HOLDER modifier) {
		Objects.requireNonNull(modifier);
		
		modifiers.add(modifier);
	}

	public List<INTERFACE_METHOD_MODIFIER_HOLDER> getModifiers() {
		return modifiers;
	}
	
}
