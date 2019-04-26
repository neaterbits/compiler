package com.neaterbits.compiler.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class ConstructorModifiers extends BaseModifiers<ConstructorModifier, ConstructorModifierHolder> {

	public ConstructorModifiers(List<ConstructorModifierHolder> modifiers) {
		super(modifiers);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CONSTRUCTOR_MODIFIERS;
	}
}
