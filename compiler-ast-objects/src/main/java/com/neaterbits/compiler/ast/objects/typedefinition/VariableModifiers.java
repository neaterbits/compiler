package com.neaterbits.compiler.ast.objects.typedefinition;

import java.util.List;

import com.neaterbits.compiler.ast.objects.annotation.Annotation;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.typedefinition.VariableModifier;

public final class VariableModifiers extends BaseModifiers<VariableModifier, VariableModifierHolder> {

	public VariableModifiers(List<Annotation> annotations, List<VariableModifierHolder> modifiers) {
		super(annotations, modifiers);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.VARIABLE_MODIFIERS;
	}
}
