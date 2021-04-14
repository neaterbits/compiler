package dev.nimbler.compiler.ast.objects.variables;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.expression.literal.Primary;

public abstract class UnresolvedPrimary extends Primary {

    protected UnresolvedPrimary(Context context) {
        super(context);
    }
}
