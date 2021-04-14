package dev.nimbler.compiler.ast.objects.typedefinition;

import java.util.List;

import dev.nimbler.compiler.ast.objects.annotation.Annotation;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.typedefinition.ClassModifier;

public final class ClassModifiers extends BaseModifiers<ClassModifier, ClassModifierHolder> {
    
	public ClassModifiers(List<Annotation> annotations, List<ClassModifierHolder> modifiers) {
		super(annotations, modifiers);
	}

    @Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CLASS_MODIFIERS;
	}
}
