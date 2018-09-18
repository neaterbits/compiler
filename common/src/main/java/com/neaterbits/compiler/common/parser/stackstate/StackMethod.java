package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.block.Method;
import com.neaterbits.compiler.common.ast.typedefinition.MethodModifierHolder;
import com.neaterbits.compiler.common.log.ParseLogger;

public final class StackMethod extends CallableStackEntry {

	private final List<MethodModifierHolder> modifiers;
	
	public StackMethod(ParseLogger parseLogger) {
		super(parseLogger);
		
		this.modifiers = new ArrayList<>();
	}

	public StackMethod(ParseLogger parseLogger, String name) {
		super(parseLogger, name);

		this.modifiers = new ArrayList<>();
	}
	
	public void addModifier(MethodModifierHolder modifier) {
		Objects.requireNonNull(modifier);

		modifiers.add(modifier);
	}

	public List<MethodModifierHolder> getModifiers() {
		return modifiers;
	}

	public Method makeMethod(Context context) {
		return new Method(context, getReturnType(), getName(), getParameters(), new Block(context, getList()));
	}
}
