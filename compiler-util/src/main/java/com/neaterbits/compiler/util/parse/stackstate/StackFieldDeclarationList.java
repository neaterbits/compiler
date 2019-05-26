package com.neaterbits.compiler.util.parse.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackFieldDeclarationList<TYPE_REFERENCE, EXPRESSION, FIELD_MODIFIER_HOLDER>
		extends BaseStackVariableDeclarationList<TYPE_REFERENCE, EXPRESSION> {

	private final List<FIELD_MODIFIER_HOLDER> modifiers;

	public StackFieldDeclarationList(ParseLogger parseLogger) {
		super(parseLogger);

		this.modifiers = new ArrayList<>();
	}

	public void addFieldModifier(FIELD_MODIFIER_HOLDER modifier) {
		Objects.requireNonNull(modifier);
		
		modifiers.add(modifier);
	}

	public List<FIELD_MODIFIER_HOLDER> getModifiers() {
		return modifiers;
	}
}
