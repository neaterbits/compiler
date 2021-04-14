package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.TypeBoundSetter;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackGenericParameter<NAME, TYPE_BOUND>
    extends ListStackEntry<TYPE_BOUND>
    implements TypeBoundSetter<TYPE_BOUND> {

    private final NAME name;

    public StackGenericParameter(ParseLogger parseLogger, NAME name) {
        super(parseLogger);
    
        Objects.requireNonNull(name);
        
        this.name = name;
    }

    public NAME getName() {
        return name;
    }

    @Override
    public void addTypeBound(TYPE_BOUND typeBound) {

        Objects.requireNonNull(typeBound);
        
        add(typeBound);
    }
}
