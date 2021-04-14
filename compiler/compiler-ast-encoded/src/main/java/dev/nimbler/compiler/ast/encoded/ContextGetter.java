package dev.nimbler.compiler.ast.encoded;

import com.neaterbits.util.parse.context.Context;

public interface ContextGetter {

    Context getElementContext(int parseTreeRef);
    
    Context getContextFromRef(int ref);
}
