package com.neaterbits.compiler.ast.objects.typedefinition;

import java.util.List;

import com.neaterbits.compiler.ast.objects.annotation.Annotation;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.types.typedefinition.ClassModifier;

public final class ClassModifiers extends BaseModifiers<ClassModifier, ClassModifierHolder> {
    
	public ClassModifiers(List<Annotation> annotations, List<ClassModifierHolder> modifiers) {
		super(annotations, modifiers);
	}

    @Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CLASS_MODIFIERS;
	}
}
