package com.neaterbits.compiler.parser.listener.common;

import com.neaterbits.compiler.util.Context;

public interface ContextWriteAccess {

    int writeContext(Context context);
    
    int writeContext(int otherContext);
}
