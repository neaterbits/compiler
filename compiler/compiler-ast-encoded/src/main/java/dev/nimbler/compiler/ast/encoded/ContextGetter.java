package dev.nimbler.compiler.ast.encoded;

import org.jutils.parse.context.Context;

public interface ContextGetter {

    Context getElementContext(int parseTreeRef);
    
    Context getContextFromRef(int ref);
}
