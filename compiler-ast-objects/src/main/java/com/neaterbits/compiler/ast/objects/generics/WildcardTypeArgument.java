package com.neaterbits.compiler.ast.objects.generics;

import java.util.Collection;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class WildcardTypeArgument extends TypeArgument {

    public WildcardTypeArgument(Context context, Collection<TypeBound> typeBounds) {
        super(context, typeBounds);
    }

    @Override
    public ParseTreeElement getParseTreeElement() {
        return ParseTreeElement.WILDCARD_GENERIC_TYPE;
    }
}
