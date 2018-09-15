package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.block.Method;
import com.neaterbits.compiler.common.ast.typedefinition.MethodModifier;

public final class StackMethod extends CallableStackEntry {

	private final List<MethodModifier> modifiers;
	
	public StackMethod() {
		this.modifiers = new ArrayList<>();
	}

	public StackMethod(String name) {
		super(name);

		this.modifiers = new ArrayList<>();
	}
	
	public void addModifier(MethodModifier modifier) {
		Objects.requireNonNull(modifier);

		modifiers.add(modifier);
	}

	public List<MethodModifier> getModifiers() {
		return modifiers;
	}

	public Method makeMethod(Context context) {
		return new Method(context, null, getName(), getParameters(), new Block(context, getList()));
	}
}
