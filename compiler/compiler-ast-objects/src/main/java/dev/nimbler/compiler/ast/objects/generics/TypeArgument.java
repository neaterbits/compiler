package dev.nimbler.compiler.ast.objects.generics;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.BaseASTElement;

public abstract class TypeArgument extends BaseASTElement {

    TypeArgument(Context context) {
        super(context);
    }
}
