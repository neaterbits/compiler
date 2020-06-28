package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.TypeReferenceSetter;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackGenericTypeParameters<TYPE_REFERENCE>
    extends ListStackEntry<TYPE_REFERENCE>
    implements TypeReferenceSetter<TYPE_REFERENCE> {

    public StackGenericTypeParameters(ParseLogger parseLogger) {
        super(parseLogger);
    }

    @Override
    public void setTypeReference(TYPE_REFERENCE typeReference) {

        Objects.requireNonNull(typeReference);
        
        add(typeReference);
    }
}
