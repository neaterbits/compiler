package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.StackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.TypeReferenceSetter;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackReferenceTypeArgument<TYPE_REFERENCE>
    extends StackEntry
    implements TypeReferenceSetter<TYPE_REFERENCE> {
    
    private TYPE_REFERENCE typeReference;
    
    public StackReferenceTypeArgument(ParseLogger parseLogger) {
        super(parseLogger);
    }

    @Override
    public void setTypeReference(TYPE_REFERENCE typeReference) {

        Objects.requireNonNull(typeReference);
        
        if (this.typeReference != null) {
            throw new IllegalStateException();
        }
        
        this.typeReference = typeReference;
    }

    public TYPE_REFERENCE getTypeReference() {
        return typeReference;
    }
}
