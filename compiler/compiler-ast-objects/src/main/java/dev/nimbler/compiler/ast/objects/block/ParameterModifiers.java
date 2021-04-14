package dev.nimbler.compiler.ast.objects.block;

import java.util.List;

import dev.nimbler.compiler.ast.objects.annotation.Annotation;
import dev.nimbler.compiler.ast.objects.typedefinition.BaseModifiers;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class ParameterModifiers
    extends BaseModifiers<ParameterModifier, ParameterModifierHolder> {

    public ParameterModifiers(List<Annotation> annotations, List<ParameterModifierHolder> modifiers) {
        super(annotations, modifiers);
    }

    @Override
    public ParseTreeElement getParseTreeElement() {
        return ParseTreeElement.PARAMETER_MODIFIERS;
    }
}
