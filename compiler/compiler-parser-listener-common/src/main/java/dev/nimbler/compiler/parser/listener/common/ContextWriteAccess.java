package dev.nimbler.compiler.parser.listener.common;

import org.jutils.parse.context.Context;

public interface ContextWriteAccess {

    int writeContext(Context context);
    
    int writeContext(int otherContext);
}
