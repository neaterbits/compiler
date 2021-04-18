package dev.nimbler.compiler.ast.objects.variables;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.expression.literal.Primary;

public abstract class ResolvedPrimary extends Primary {

    protected ResolvedPrimary(Context context) {
        super(context);
    }
}
