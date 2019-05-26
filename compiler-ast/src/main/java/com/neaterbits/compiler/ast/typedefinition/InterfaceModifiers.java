package com.neaterbits.compiler.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.typedefinition.InterfaceModifier;

public final class InterfaceModifiers extends BaseModifiers<InterfaceModifier, InterfaceModifierHolder> {

	public InterfaceModifiers(List<InterfaceModifierHolder> modifiers) {
		
		super(modifiers);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.INTERFACE_MODIFIERS;
	}
}
