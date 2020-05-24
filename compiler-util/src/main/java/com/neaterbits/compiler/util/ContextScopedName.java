package com.neaterbits.compiler.util;

import java.util.Objects;

public final class ContextScopedName {

    private final Context context;
    private final ScopedName scopedName;
    
    public ContextScopedName(Context context, ScopedName scopedName) {

        // Objects.requireNonNull(context);
        Objects.requireNonNull(scopedName);
        
        this.context = context;
        this.scopedName = scopedName;
    }

    public Context getContext() {
        return context;
    }

    public ScopedName getScopedName() {
        return scopedName;
    }
}
