package com.neaterbits.compiler.ast.objects.generics;

import java.util.Collection;

import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Context;

public final class WildcardTypeArgument extends TypeBoundTypeArgument {

    public WildcardTypeArgument(Context context, Collection<TypeBound> typeBounds) {
        super(context, typeBounds);
    }

    @Override
    public ParseTreeElement getParseTreeElement() {
        return ParseTreeElement.WILDCARD_GENERIC_TYPE_ARGUMENT;
    }
}
