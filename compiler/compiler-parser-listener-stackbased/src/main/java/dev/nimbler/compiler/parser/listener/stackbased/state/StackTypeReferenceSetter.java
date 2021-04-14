package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.StackEntry;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.TypeReferenceSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

public class StackTypeReferenceSetter<TYPE_REFERENCE>
    extends StackEntry
    implements TypeReferenceSetter<TYPE_REFERENCE> {


    private TYPE_REFERENCE typeReference;
        
    public StackTypeReferenceSetter(ParseLogger parseLogger) {
        super(parseLogger);
    }

    public final TYPE_REFERENCE getTypeReference() {
        return typeReference;
    }

    @Override
    public final void setTypeReference(TYPE_REFERENCE typeReference) {

        Objects.requireNonNull(typeReference);
        
        if (this.typeReference != null) {
            throw new IllegalStateException();
        }
        
        this.typeReference = typeReference;
    }
}
