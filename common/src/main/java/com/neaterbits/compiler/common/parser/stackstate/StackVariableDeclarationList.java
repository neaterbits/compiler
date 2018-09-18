package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.typedefinition.VariableModifierHolder;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.VariableModifierSetter;

public final class StackVariableDeclarationList
	extends BaseStackVariableDeclarationList
	implements VariableModifierSetter {

	private final List<VariableModifierHolder> modifiers;

	public StackVariableDeclarationList(ParseLogger parseLogger) {
		super(parseLogger);

		this.modifiers = new ArrayList<>();
	}

	public List<VariableModifierHolder> getModifiers() {
		return modifiers;
	}

	@Override
	public void addModifier(VariableModifierHolder modifier) {
		Objects.requireNonNull(modifier);
		
		modifiers.add(modifier);
	}
}
