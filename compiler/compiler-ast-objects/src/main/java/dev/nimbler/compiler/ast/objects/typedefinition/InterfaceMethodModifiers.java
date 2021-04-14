package dev.nimbler.compiler.ast.objects.typedefinition;

import java.util.List;

import dev.nimbler.compiler.ast.objects.annotation.Annotation;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.typedefinition.InterfaceMethodModifier;

public final class InterfaceMethodModifiers extends BaseModifiers<InterfaceMethodModifier, InterfaceMethodModifierHolder> {

	public InterfaceMethodModifiers(List<Annotation> annotations, List<InterfaceMethodModifierHolder> modifiers) {
		super(annotations, modifiers);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.INTERFACE_METHOD_MODIFIERS;
	}
}
