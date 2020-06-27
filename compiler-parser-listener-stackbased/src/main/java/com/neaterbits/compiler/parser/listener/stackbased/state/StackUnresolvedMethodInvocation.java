package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.method.MethodInvocationType;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackUnresolvedMethodInvocation<EXPRESSION, PRIMARY extends EXPRESSION, NAME_LIST>
        extends CallStackEntry<EXPRESSION> {

    private final MethodInvocationType type;
    private final NAME_LIST names;

    public StackUnresolvedMethodInvocation(ParseLogger parseLogger, MethodInvocationType type, NAME_LIST names,
            String methodName, Context methodNameContext) {
        super(parseLogger);

        Objects.requireNonNull(type);
        Objects.requireNonNull(methodName);
        Objects.requireNonNull(methodNameContext);

        setName(methodName, methodNameContext);

        this.names = names;
        this.type = type;
    }

    public MethodInvocationType getType() {
        return type;
    }

    public NAME_LIST getNames() {
        return names;
    }
}
