package com.neaterbits.compiler.parser.listener.stackbased.state;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackWildcardTypeArgument<TYPE_BOUND> extends StackTypeBoundTypeArgument<TYPE_BOUND> {

    public StackWildcardTypeArgument(ParseLogger parseLogger, Context context) {
        super(parseLogger, context);
    }
}
