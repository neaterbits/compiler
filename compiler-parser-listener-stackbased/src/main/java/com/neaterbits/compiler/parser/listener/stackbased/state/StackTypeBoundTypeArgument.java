package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.TypeBoundSetter;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public abstract class StackTypeBoundTypeArgument<TYPE_BOUND>
    extends ListStackEntry<TYPE_BOUND>
    implements TypeBoundSetter<TYPE_BOUND> {

    private final Context context;

    public StackTypeBoundTypeArgument(ParseLogger parseLogger, Context context) {
        super(parseLogger);
        
        this.context = context;
    }

    public final Context getContext() {
        return context;
    }

    @Override
    public final void addTypeBound(TYPE_BOUND typeBound) {
        
        Objects.requireNonNull(typeBound);
        
        add(typeBound);
    }
}
