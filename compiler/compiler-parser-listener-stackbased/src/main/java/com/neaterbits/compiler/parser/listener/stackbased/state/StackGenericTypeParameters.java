package com.neaterbits.compiler.parser.listener.stackbased.state;


import com.neaterbits.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackGenericTypeParameters<GENERIC_PARAMETER> extends ListStackEntry<GENERIC_PARAMETER> {

    public StackGenericTypeParameters(ParseLogger parseLogger) {
        super(parseLogger);
    }
}
