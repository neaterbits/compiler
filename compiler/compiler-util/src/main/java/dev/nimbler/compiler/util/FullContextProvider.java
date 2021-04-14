package dev.nimbler.compiler.util;

import com.neaterbits.util.parse.context.Context;
import com.neaterbits.util.parse.context.FullContext;

public interface FullContextProvider {

    FullContext makeFullContext(Context context);
    
    default String getText(Context context) {
        return makeFullContext(context).getText();
    }
    
    default int getLength(Context context) {
        return makeFullContext(context).getLength();
    }
}
