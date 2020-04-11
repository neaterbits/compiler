package com.neaterbits.compiler.ast.objects.typedefinition;

import java.util.List;

import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.typedefinition.ConstructorModifier;

public final class ConstructorModifiers extends BaseModifiers<ConstructorModifier, ConstructorModifierHolder> {

	public ConstructorModifiers(List<ConstructorModifierHolder> modifiers) {
		super(modifiers);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CONSTRUCTOR_MODIFIERS;
	}
}