package dev.nimbler.compiler.util;

import org.jutils.parse.context.Context;
import org.jutils.parse.context.FullContext;

public interface FullContextProvider {

    FullContext makeFullContext(Context context);
    
    default String getText(Context context) {
        return makeFullContext(context).getText();
    }
    
    default int getLength(Context context) {
        return makeFullContext(context).getLength();
    }
}
