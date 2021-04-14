package dev.nimbler.compiler.ast.objects.generics;

import java.util.Collection;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.types.ParseTreeElement;

public final class WildcardTypeArgument extends TypeBoundTypeArgument {

    public WildcardTypeArgument(Context context, Collection<TypeBound> typeBounds) {
        super(context, typeBounds);
    }

    @Override
    public ParseTreeElement getParseTreeElement() {
        return ParseTreeElement.WILDCARD_GENERIC_TYPE_ARGUMENT;
    }
}
