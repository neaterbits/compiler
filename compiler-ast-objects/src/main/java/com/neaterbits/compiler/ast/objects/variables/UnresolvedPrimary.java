package com.neaterbits.compiler.ast.objects.variables;

import com.neaterbits.compiler.ast.objects.expression.literal.Primary;
import com.neaterbits.util.parse.context.Context;

public abstract class UnresolvedPrimary extends Primary {

    protected UnresolvedPrimary(Context context) {
        super(context);
    }
}
