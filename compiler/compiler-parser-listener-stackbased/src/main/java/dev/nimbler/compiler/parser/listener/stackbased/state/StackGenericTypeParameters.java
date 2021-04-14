package dev.nimbler.compiler.parser.listener.stackbased.state;


import dev.nimbler.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackGenericTypeParameters<GENERIC_PARAMETER> extends ListStackEntry<GENERIC_PARAMETER> {

    public StackGenericTypeParameters(ParseLogger parseLogger) {
        super(parseLogger);
    }
}
