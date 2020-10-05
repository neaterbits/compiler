package com.neaterbits.compiler.ast.objects.generics;

import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.util.parse.context.Context;

public abstract class TypeArgument extends BaseASTElement {

    TypeArgument(Context context) {
        super(context);
    }
}
