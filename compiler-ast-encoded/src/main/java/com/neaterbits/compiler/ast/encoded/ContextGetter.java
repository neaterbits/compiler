package com.neaterbits.compiler.ast.encoded;

import com.neaterbits.compiler.util.Context;

public interface ContextGetter {

    Context getElementContext(int parseTreeRef);
    
    Context getContextFromRef(int ref);
}
