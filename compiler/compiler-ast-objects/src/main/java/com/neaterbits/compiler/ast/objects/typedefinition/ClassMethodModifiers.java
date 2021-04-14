package com.neaterbits.compiler.ast.objects.typedefinition;

import java.util.List;

import com.neaterbits.compiler.ast.objects.annotation.Annotation;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.types.typedefinition.ClassMethodModifier;

public final class ClassMethodModifiers extends BaseModifiers<ClassMethodModifier, ClassMethodModifierHolder> {

	public ClassMethodModifiers(List<Annotation> annotations, List<ClassMethodModifierHolder> modifiers) {
		super(annotations, modifiers);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CLASS_METHOD_MODIFIERS;
	}
}
