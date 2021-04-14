package dev.nimbler.compiler.ast.objects.block;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.typedefinition.BaseModifierHolder;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class ParameterModifierHolder extends BaseModifierHolder<ParameterModifier> {

    public ParameterModifierHolder(Context context, ParameterModifier delegate) {
        super(context, delegate);
    }

    @Override
    public ParseTreeElement getParseTreeElement() {
        return ParseTreeElement.PARAMETER_MODIFIER_HOLDER;
    }
}
