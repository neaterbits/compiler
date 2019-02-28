package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceMethod;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceMethodModifierHolder;
import com.neaterbits.compiler.common.log.ParseLogger;

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
