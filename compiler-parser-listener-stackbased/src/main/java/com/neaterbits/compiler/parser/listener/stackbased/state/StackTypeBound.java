package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.types.typedefinition.TypeBoundType;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackTypeBound<NAME, TYPE_REFERENCE> extends StackTypeReferenceSetter<TYPE_REFERENCE> {

    private final TypeBoundType type;
    
    public StackTypeBound(ParseLogger parseLogger, TypeBoundType type) {
        super(parseLogger);
    
        Objects.requireNonNull(type);
        
        this.type = type;
    }

    public TypeBoundType getType() {
        return type;
    }
}
