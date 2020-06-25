package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.method.MethodInvocationType;
import com.neaterbits.compiler.util.name.Names;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackUnresolvedMethodInvocation<EXPRESSION, PRIMARY extends EXPRESSION>
        extends CallStackEntry<EXPRESSION> {

    private final MethodInvocationType type;
    private final Names names;

    public StackUnresolvedMethodInvocation(ParseLogger parseLogger, MethodInvocationType type, Names names,
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

    public Names getNames() {
        return names;
    }
}
