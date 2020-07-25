package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.TypeReferenceSetter;
import com.neaterbits.compiler.util.parse.ParseLogger;

public class StackTypeReferenceList<TYPE_REFERENCE>
    extends ListStackEntry<TYPE_REFERENCE>
    implements TypeReferenceSetter<TYPE_REFERENCE> {

    public StackTypeReferenceList(ParseLogger parseLogger) {
        super(parseLogger);
    }

    @Override
    public final void setTypeReference(TYPE_REFERENCE typeReference) {

        Objects.requireNonNull(typeReference);
        
        add(typeReference);
    }
}
