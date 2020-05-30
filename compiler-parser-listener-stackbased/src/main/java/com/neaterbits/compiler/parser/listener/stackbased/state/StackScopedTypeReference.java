package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.StackEntry;
import com.neaterbits.compiler.util.model.ReferenceType;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackScopedTypeReference extends StackEntry {

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
