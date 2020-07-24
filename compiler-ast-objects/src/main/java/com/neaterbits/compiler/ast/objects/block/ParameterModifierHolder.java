package com.neaterbits.compiler.ast.objects.block;

import com.neaterbits.compiler.ast.objects.typedefinition.BaseModifierHolder;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class ParameterModifierHolder extends BaseModifierHolder<ParameterModifier> {

    public ParameterModifierHolder(Context context, ParameterModifier delegate) {
        super(context, delegate);
    }

    @Override
    public ParseTreeElement getParseTreeElement() {
        return ParseTreeElement.PARAMETER_MODIFIER_HOLDER;
    }
}
