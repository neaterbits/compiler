package dev.nimbler.compiler.ast.objects.typedefinition;

import java.util.List;

import dev.nimbler.compiler.ast.objects.annotation.Annotation;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.typedefinition.InterfaceModifier;

public final class InterfaceModifiers extends BaseModifiers<InterfaceModifier, InterfaceModifierHolder> {

	public InterfaceModifiers(List<Annotation> annotations, List<InterfaceModifierHolder> modifiers) {
		
		super(annotations, modifiers);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.INTERFACE_MODIFIERS;
	}
}
