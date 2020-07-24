package com.neaterbits.compiler.ast.objects.typedefinition;

import java.util.List;

import com.neaterbits.compiler.ast.objects.annotation.Annotation;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.typedefinition.InterfaceModifier;

public final class InterfaceModifiers extends BaseModifiers<InterfaceModifier, InterfaceModifierHolder> {

	public InterfaceModifiers(List<Annotation> annotations, List<InterfaceModifierHolder> modifiers) {
		
		super(annotations, modifiers);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.INTERFACE_MODIFIERS;
	}
}
