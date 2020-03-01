package com.neaterbits.compiler.ast.objects.typedefinition;

import java.util.List;

import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.typedefinition.ClassMethodModifier;

public final class ClassMethodModifiers extends BaseModifiers<ClassMethodModifier, ClassMethodModifierHolder> {

	public ClassMethodModifiers(List<ClassMethodModifierHolder> modifiers) {
		super(modifiers);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CLASS_METHOD_MODIFIERS;
	}
}
