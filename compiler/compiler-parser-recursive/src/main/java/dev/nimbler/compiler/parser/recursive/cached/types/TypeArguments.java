package dev.nimbler.compiler.parser.recursive.cached.types;

import com.neaterbits.util.parse.context.Context;

// Interface for getting type arguments, either generic type or reference type
// eg. Function<SOME_GENERIC_TYPE, Map<String, SOME_OTHER_GENERIC_TYPE>
// where the first is a generic type name while the other is a
// reference type name but again with generic or reference type arguments


public interface TypeArguments {
    
    int getStartContext();
    
    Context getEndContext();

    TypeArgument getTypeArgument(int index);
    
    int count();
}
