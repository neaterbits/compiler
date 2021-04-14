package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import dev.nimbler.compiler.types.ReferenceType;
import dev.nimbler.compiler.util.parse.ParseLogger;
import dev.nimbler.language.common.types.ScopedName;

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
