package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.types.ReferenceType;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackScopedTypeReference<TYPE_REFERENCE> extends StackTypeReference<TYPE_REFERENCE> {

    private final ReferenceType referenceType;

    private ScopedName scopedName;

    public StackScopedTypeReference(ParseLogger parseLogger, ReferenceType referenceType) {
        super(parseLogger);

        Objects.requireNonNull(referenceType);
        
        this.referenceType = referenceType;
    }

    public ScopedName getScopedName() {
        return scopedName;
    }

    public void setScopedName(ScopedName scopedName) {
        this.scopedName = scopedName;
    }

    public ReferenceType getReferenceType() {
        return referenceType;
    }
}
