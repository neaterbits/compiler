package dev.nimbler.compiler.ast.objects.typedefinition;

import java.util.List;

import dev.nimbler.compiler.ast.objects.annotation.Annotation;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.typedefinition.FieldModifier;

public final class FieldModifiers extends BaseModifiers<FieldModifier, FieldModifierHolder> {
	
	public FieldModifiers(List<Annotation> annotations, List<FieldModifierHolder> modifiers) {
		super(annotations, modifiers);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.FIELD_MODIFIERS;
	}
}
