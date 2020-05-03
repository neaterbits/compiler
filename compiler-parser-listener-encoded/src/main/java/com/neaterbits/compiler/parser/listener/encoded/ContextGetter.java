package com.neaterbits.compiler.parser.listener.encoded;

import com.neaterbits.compiler.util.Context;

public interface ContextGetter {

    Context getElementContext(int parseTreeRef);
    
    Context getContextFromRef(int ref);
}
