package com.neaterbits.compiler.parser.listener.stackbased.state;

import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackReferenceTypeArgument<TYPE_REFERENCE> extends StackTypeReferenceSetter<TYPE_REFERENCE> {
    
    public StackReferenceTypeArgument(ParseLogger parseLogger) {
        super(parseLogger);
    }
}
