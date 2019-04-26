package com.neaterbits.compiler.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class ClassModifiers extends BaseModifiers<ClassModifier, ClassModifierHolder> {
	
	public ClassModifiers(List<ClassModifierHolder> modifiers) {
		super(modifiers);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CLASS_MODIFIERS;
	}
}
