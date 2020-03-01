package com.neaterbits.compiler.ast.objects.typedefinition;

import java.util.List;

import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.typedefinition.VariableModifier;

public final class VariableModifiers extends BaseModifiers<VariableModifier, VariableModifierHolder> {

	public VariableModifiers(List<VariableModifierHolder> modifiers) {
		super(modifiers);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.VARIABLE_MODIFIERS;
	}
}
