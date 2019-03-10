package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.block.Block;
import com.neaterbits.compiler.ast.block.ClassMethod;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodModifierHolder;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackClassMethod extends CallableStackEntry {

	private final List<ClassMethodModifierHolder> modifiers;
	
	public StackClassMethod(ParseLogger parseLogger) {
		super(parseLogger);
		
		this.modifiers = new ArrayList<>();
	}

	public StackClassMethod(ParseLogger parseLogger, String name) {
		super(parseLogger, name);

		this.modifiers = new ArrayList<>();
	}
	
	public void addModifier(ClassMethodModifierHolder modifier) {
		Objects.requireNonNull(modifier);

		modifiers.add(modifier);
	}

	public List<ClassMethodModifierHolder> getModifiers() {
		return modifiers;
	}

	public ClassMethod makeMethod(Context context) {
		return new ClassMethod(context, getReturnType(), getName(), getParameters(), new Block(context, getList()));
	}
}
