package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.types.ReferenceType;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackNonScopedTypeReference<TYPE_REFERENCE> extends StackTypeReference<TYPE_REFERENCE> {

    private final String name;
    private final ReferenceType referenceType;
    
    public StackNonScopedTypeReference(ParseLogger parseLogger, String name, ReferenceType referenceType) {
        super(parseLogger);
        
        Objects.requireNonNull(name);
        Objects.requireNonNull(referenceType);
        
        this.name = name;
        this.referenceType = referenceType;
    }

    public String getName() {
        return name;
    }

    public ReferenceType getReferenceType() {
        return referenceType;
    }
}
