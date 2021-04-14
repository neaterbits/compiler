package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import dev.nimbler.compiler.util.parse.ParseLogger;
import dev.nimbler.language.common.types.ScopedName;

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
