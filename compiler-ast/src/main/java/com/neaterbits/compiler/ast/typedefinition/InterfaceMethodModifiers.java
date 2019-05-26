package com.neaterbits.compiler.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.typedefinition.InterfaceMethodModifier;

public final class InterfaceMethodModifiers extends BaseModifiers<InterfaceMethodModifier, InterfaceMethodModifierHolder> {

	public InterfaceMethodModifiers(List<InterfaceMethodModifierHolder> modifiers) {
		super(modifiers);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.INTERFACE_METHOD_MODIFIERS;
	}
}
