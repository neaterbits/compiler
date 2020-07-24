package com.neaterbits.compiler.ast.objects.typedefinition;

import java.util.List;

import com.neaterbits.compiler.ast.objects.annotation.Annotation;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.typedefinition.InterfaceMethodModifier;

public final class InterfaceMethodModifiers extends BaseModifiers<InterfaceMethodModifier, InterfaceMethodModifierHolder> {

	public InterfaceMethodModifiers(List<Annotation> annotations, List<InterfaceMethodModifierHolder> modifiers) {
		super(annotations, modifiers);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.INTERFACE_METHOD_MODIFIERS;
	}
}
