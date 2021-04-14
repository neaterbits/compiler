package dev.nimbler.compiler.ast.objects.typedefinition;

import java.util.List;

import dev.nimbler.compiler.ast.objects.annotation.Annotation;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.typedefinition.ClassMethodModifier;

public final class ClassMethodModifiers extends BaseModifiers<ClassMethodModifier, ClassMethodModifierHolder> {

	public ClassMethodModifiers(List<Annotation> annotations, List<ClassMethodModifierHolder> modifiers) {
		super(annotations, modifiers);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CLASS_METHOD_MODIFIERS;
	}
}
