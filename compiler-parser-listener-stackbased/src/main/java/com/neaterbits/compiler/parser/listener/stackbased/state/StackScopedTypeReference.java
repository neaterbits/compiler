package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.util.model.ReferenceType;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackScopedTypeReference<TYPE_REFERENCE> extends StackTypeReference<TYPE_REFERENCE> {

    private final ReferenceType referenceType;

    public StackScopedTypeReference(ParseLogger parseLogger, ReferenceType referenceType) {
        super(parseLogger);

        Objects.requireNonNull(referenceType);
        
        this.referenceType = referenceType;
    }

    public ReferenceType getReferenceType() {
        return referenceType;
    }
}
