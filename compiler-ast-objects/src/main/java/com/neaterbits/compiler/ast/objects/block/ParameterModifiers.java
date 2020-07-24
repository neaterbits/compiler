package com.neaterbits.compiler.ast.objects.block;

import java.util.List;

import com.neaterbits.compiler.ast.objects.annotation.Annotation;
import com.neaterbits.compiler.ast.objects.typedefinition.BaseModifiers;
import com.neaterbits.compiler.util.model.ParseTreeElement;

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
