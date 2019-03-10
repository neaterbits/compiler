package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.typedefinition.InterfaceMethod;
import com.neaterbits.compiler.ast.typedefinition.InterfaceMethodModifierHolder;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackInterfaceMethod extends CallableStackEntry {

	private final List<InterfaceMethodModifierHolder> modifiers;

	public StackInterfaceMethod(ParseLogger parseLogger, String name) {
		super(parseLogger, name);

		this.modifiers = new ArrayList<>();
	}

	public StackInterfaceMethod(ParseLogger parseLogger) {
		super(parseLogger);
		
		this.modifiers = new ArrayList<>();
	}
	
	public void addModifier(InterfaceMethodModifierHolder modifier) {
		Objects.requireNonNull(modifier);
		
		modifiers.add(modifier);
	}

	public List<InterfaceMethodModifierHolder> getModifiers() {
		return modifiers;
	}
	
	public InterfaceMethod makeMethod(Context context) {
		return new InterfaceMethod(context, getReturnType(), getName(), getParameters());
	}
}
