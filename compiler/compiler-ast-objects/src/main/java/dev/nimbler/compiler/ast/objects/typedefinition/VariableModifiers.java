package dev.nimbler.compiler.ast.objects.typedefinition;

import java.util.List;

import dev.nimbler.compiler.ast.objects.annotation.Annotation;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.typedefinition.VariableModifier;

public final class VariableModifiers extends BaseModifiers<VariableModifier, VariableModifierHolder> {

	public VariableModifiers(List<Annotation> annotations, List<VariableModifierHolder> modifiers) {
		super(annotations, modifiers);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.VARIABLE_MODIFIERS;
	}
}
