package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.StackEntry;
import com.neaterbits.compiler.util.parse.ParseLogger;

public abstract class StackTypeReference<TYPE_REFERENCE> extends StackEntry {

    private final List<TYPE_REFERENCE> genericTypeParameters;
    
    public StackTypeReference(ParseLogger parseLogger) {
        super(parseLogger);
    
        this.genericTypeParameters = new ArrayList<>();
    }

    public final List<TYPE_REFERENCE> getGenericTypeParameters() {
        return genericTypeParameters;
    }

    public final void setTypeParameters(Collection<TYPE_REFERENCE> typeParameters) {

        Objects.requireNonNull(typeParameters);
        
        if (!genericTypeParameters.isEmpty()) {
            throw new IllegalStateException();
        }

        genericTypeParameters.addAll(typeParameters);
    }
}
