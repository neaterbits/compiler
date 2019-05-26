package com.neaterbits.compiler.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.typedefinition.FieldModifier;

public final class FieldModifiers extends BaseModifiers<FieldModifier, FieldModifierHolder> {
	
	public FieldModifiers(List<FieldModifierHolder> modifiers) {
		super(modifiers);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.FIELD_MODIFIERS;
	}
}
