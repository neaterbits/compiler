package com.neaterbits.compiler.util;

public class CastFullContextProvider implements FullContextProvider {

    public static final CastFullContextProvider INSTANCE = new CastFullContextProvider();
    
    private CastFullContextProvider() {
    }
    
    @Override
    public FullContext makeFullContext(Context context) {
        return (FullContext)context;
    }
}
