package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackNamedTypeArgument<NAME, TYPE_BOUND> extends StackGenericTypeArgument<TYPE_BOUND> {

    private final NAME name;
    
    public StackNamedTypeArgument(ParseLogger parseLogger, Context context, NAME name) {
        super(parseLogger, context);
        
        Objects.requireNonNull(name);
        
        this.name = name;
    }

    public NAME getName() {
        return name;
    }
}
