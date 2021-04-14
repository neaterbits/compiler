package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackGenericTypeArgumentList<TYPE_ARGUMENT> extends ListStackEntry<TYPE_ARGUMENT> {

    public StackGenericTypeArgumentList(ParseLogger parseLogger) {
        super(parseLogger);
    }

    public void addTypeArgument(TYPE_ARGUMENT typeArgument) {
        
        Objects.requireNonNull(typeArgument);
        
        add(typeArgument);
    }
}
