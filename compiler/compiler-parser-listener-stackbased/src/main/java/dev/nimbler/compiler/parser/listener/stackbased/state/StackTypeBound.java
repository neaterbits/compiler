package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import dev.nimbler.compiler.types.typedefinition.TypeBoundType;
import dev.nimbler.compiler.util.parse.ParseLogger;

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
