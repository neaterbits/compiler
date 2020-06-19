package com.neaterbits.compiler.parser.listener.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.Context;

public class ListContextAccess extends ContextAccess {

    private final List<Context> writtenContexts;

    public ListContextAccess() {
        this.writtenContexts = new ArrayList<>();
    }

    @Override
    public Context getContext(int index) {

        return writtenContexts.get(index);
    }

    @Override
    public final int writeContext(Context context) {

        Objects.requireNonNull(context);
        
        final int index = writtenContexts.size();
        
        writtenContexts.add(context);
                
        return index;
    }

    @Override
    public final int writeContext(int otherContext) {
        
        final Context context = getContext(otherContext);

        return writeContext(context);
    }
}