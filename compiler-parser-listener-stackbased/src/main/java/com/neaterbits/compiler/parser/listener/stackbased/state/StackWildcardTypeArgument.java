package com.neaterbits.compiler.parser.listener.stackbased.state;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.parse.context.Context;

public final class StackWildcardTypeArgument<TYPE_BOUND> extends StackTypeBoundTypeArgument<TYPE_BOUND> {

    public StackWildcardTypeArgument(ParseLogger parseLogger, Context context) {
        super(parseLogger, context);
    }
}
