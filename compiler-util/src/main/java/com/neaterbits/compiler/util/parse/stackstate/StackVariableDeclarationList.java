package com.neaterbits.compiler.util.parse.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.stackstate.setters.VariableModifierSetter;

public final class StackVariableDeclarationList<TYPE_REFERENCE, EXPRESSION, MODIFIER_HOLDER>
	extends BaseStackVariableDeclarationList<TYPE_REFERENCE, EXPRESSION>
	implements VariableModifierSetter<MODIFIER_HOLDER> {

	private final List<MODIFIER_HOLDER> modifiers;

	public StackVariableDeclarationList(ParseLogger parseLogger) {
		super(parseLogger);

		this.modifiers = new ArrayList<>();
	}

	public List<MODIFIER_HOLDER> getModifiers() {
		return modifiers;
	}

	@Override
	public void addModifier(MODIFIER_HOLDER modifier) {
		Objects.requireNonNull(modifier);
		
		modifiers.add(modifier);
	}
}
