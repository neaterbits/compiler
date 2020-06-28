package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.StackEntry;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.typedefinition.TypeBoundType;

public final class StackTypeBound<NAME> extends StackEntry {

    private final TypeBoundType type;
    private final ScopedName scopedName;

    public StackTypeBound(ParseLogger parseLogger, TypeBoundType type, ScopedName scopedName) {
        super(parseLogger);
    
        Objects.requireNonNull(type);
        Objects.requireNonNull(scopedName);
        
        this.type = type;
        this.scopedName = scopedName;
    }

    public TypeBoundType getType() {
        return type;
    }

    public ScopedName getScopedName() {
        return scopedName;
    }
}
