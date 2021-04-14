package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.StackEntry;
import dev.nimbler.compiler.util.parse.ParseLogger;

public abstract class StackTypeReference<TYPE_ARGUMENT> extends StackEntry {

    private final List<TYPE_ARGUMENT> genericTypeParameters;
    
    public StackTypeReference(ParseLogger parseLogger) {
        super(parseLogger);
    
        this.genericTypeParameters = new ArrayList<>();
    }

    public final List<TYPE_ARGUMENT> getGenericTypeParameters() {
        return genericTypeParameters;
    }

    public final void setTypeParameters(Collection<TYPE_ARGUMENT> typeParameters) {

        Objects.requireNonNull(typeParameters);
        
        if (!genericTypeParameters.isEmpty()) {
            throw new IllegalStateException();
        }

        genericTypeParameters.addAll(typeParameters);
    }
}
