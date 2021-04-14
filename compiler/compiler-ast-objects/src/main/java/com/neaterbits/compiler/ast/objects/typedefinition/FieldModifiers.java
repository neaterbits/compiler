package com.neaterbits.compiler.ast.objects.typedefinition;

import java.util.List;

import com.neaterbits.compiler.ast.objects.annotation.Annotation;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.types.typedefinition.FieldModifier;

public final class FieldModifiers extends BaseModifiers<FieldModifier, FieldModifierHolder> {
	
	public FieldModifiers(List<Annotation> annotations, List<FieldModifierHolder> modifiers) {
		super(annotations, modifiers);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.FIELD_MODIFIERS;
	}
}
