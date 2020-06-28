package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.StackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.TypeReferenceSetter;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.typedefinition.TypeBoundType;

public final class StackTypeBound<NAME, TYPE_REFERENCE> extends StackEntry implements TypeReferenceSetter<TYPE_REFERENCE> {

    private final TypeBoundType type;
    
    private TYPE_REFERENCE typeReference;
    
    public StackTypeBound(ParseLogger parseLogger, TypeBoundType type) {
        super(parseLogger);
    
        Objects.requireNonNull(type);
        
        this.type = type;
    }

    public TypeBoundType getType() {
        return type;
    }

    public TYPE_REFERENCE getTypeReference() {
        return typeReference;
    }

    @Override
    public void setTypeReference(TYPE_REFERENCE typeReference) {

        Objects.requireNonNull(typeReference);
        
        if (this.typeReference != null) {
            throw new IllegalStateException();
        }
        
        this.typeReference = typeReference;
    }
}
