package com.neaterbits.compiler.util;

import com.neaterbits.util.parse.context.Context;
import com.neaterbits.util.parse.context.FullContext;

public class CastFullContextProvider implements FullContextProvider {

    public static final CastFullContextProvider INSTANCE = new CastFullContextProvider();
    
    private CastFullContextProvider() {
    }
    
    @Override
    public FullContext makeFullContext(Context context) {
        return (FullContext)context;
    }
}
