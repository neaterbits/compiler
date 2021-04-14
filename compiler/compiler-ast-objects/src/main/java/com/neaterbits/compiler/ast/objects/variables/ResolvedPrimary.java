package com.neaterbits.compiler.ast.objects.variables;

import com.neaterbits.compiler.ast.objects.expression.literal.Primary;
import com.neaterbits.util.parse.context.Context;

public abstract class ResolvedPrimary extends Primary {

    protected ResolvedPrimary(Context context) {
        super(context);
    }
}
