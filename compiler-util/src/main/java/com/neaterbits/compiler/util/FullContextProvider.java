package com.neaterbits.compiler.util;

public interface FullContextProvider {

    FullContext makeFullContext(Context context);
    
    default String getText(Context context) {
        return makeFullContext(context).getText();
    }
    
    default int getLength(Context context) {
        return makeFullContext(context).getLength();
    }
}