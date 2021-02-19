package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.block.ClassMethod;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodModifierHolder;
import com.neaterbits.compiler.common.log.ParseLogger;

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
