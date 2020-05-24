package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackScopedName extends ListStackEntry<String> {

    private final List<String> parts;
    private final List<Context> partContexts;
    
    public StackScopedName(ParseLogger parseLogger) {
        
        super(parseLogger);

        this.parts = new ArrayList<>();
        this.partContexts = new ArrayList<>();
    }
    
    public void addPart(String part, Context partContext) {
        
        Objects.requireNonNull(part);
        Objects.requireNonNull(partContext);

        parts.add(part);
        partContexts.add(partContext);
    }

    public List<String> getParts() {
        return parts;
    }
    
    public ScopedName getScopedName() {
        return ScopedName.makeScopedName(parts);
    }

    public Context getNameContext() {
        return partContexts.size() > 1
                ? Context.merge(partContexts, Function.identity())
                : partContexts.get(0);
    }
}
