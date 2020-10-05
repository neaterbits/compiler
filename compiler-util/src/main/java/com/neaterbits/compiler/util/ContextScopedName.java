package com.neaterbits.compiler.util;

import java.util.Objects;

import com.neaterbits.build.types.ScopedName;
import com.neaterbits.util.parse.context.Context;

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
