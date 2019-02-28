package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.typedefinition.FieldModifierHolder;
import com.neaterbits.compiler.common.log.ParseLogger;

public final class StackFieldDeclarationList
		extends BaseStackVariableDeclarationList {

	private final List<FieldModifierHolder> modifiers;

	public StackFieldDeclarationList(ParseLogger parseLogger) {
		super(parseLogger);

		this.modifiers = new ArrayList<>();
	}

	public void addFieldModifier(FieldModifierHolder modifier) {
		Objects.requireNonNull(modifier);
		
		modifiers.add(modifier);
	}

	public List<FieldModifierHolder> getModifiers() {
		return modifiers;
	}
}
