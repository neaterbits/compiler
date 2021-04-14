package dev.nimbler.compiler.parser.listener.stackbased.state;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackWildcardTypeArgument<TYPE_BOUND> extends StackTypeBoundTypeArgument<TYPE_BOUND> {

    public StackWildcardTypeArgument(ParseLogger parseLogger, Context context) {
        super(parseLogger, context);
    }
}
