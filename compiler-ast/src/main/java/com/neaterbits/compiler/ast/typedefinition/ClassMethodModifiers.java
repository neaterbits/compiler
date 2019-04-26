package com.neaterbits.compiler.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class ClassMethodModifiers extends BaseModifiers<ClassMethodModifier, ClassMethodModifierHolder> {

	public ClassMethodModifiers(List<ClassMethodModifierHolder> modifiers) {
		super(modifiers);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CLASS_METHOD_MODIFIERS;
	}
}
