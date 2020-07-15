package com.neaterbits.compiler.parser.recursive.cached.types;

import com.neaterbits.compiler.util.Context;

// Interface for getting type arguments, either genric type or concrete type
// eg. Function<SOME_GENERIC_TYPE, Map<String, SOME_OTHER_GENERIC_TYPE>
// where the first is a generic type name while the other is a
// concrete type name but again with generic or concrete type arguments


public interface TypeArguments {
    
    int getStartContext();
    
    Context getEndContext();

    TypeArgument getTypeArgument(int index);
    
    int count();
}
