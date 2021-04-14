package dev.nimbler.compiler.ast.objects.typedefinition;

import java.util.List;

import dev.nimbler.compiler.ast.objects.annotation.Annotation;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.typedefinition.ConstructorModifier;

public final class ConstructorModifiers extends BaseModifiers<ConstructorModifier, ConstructorModifierHolder> {

	public ConstructorModifiers(List<Annotation> annotations, List<ConstructorModifierHolder> modifiers) {
		super(annotations, modifiers);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CONSTRUCTOR_MODIFIERS;
	}
}
