package com.neaterbits.compiler.parser.recursive.cached.types;

import com.neaterbits.compiler.parser.recursive.cached.names.NamesList;
import com.neaterbits.compiler.util.Context;

public interface TypeArgument {

    boolean isGenericTypeName();
    
    long getGenericTypeName();
    
    int getGenericTypeNameContext();
    
    // if isGenericTypeName() is false
    NamesList getConcreteTypeNames();
    
    TypeArgumentsList getConcreteTypeGenerics();
    
    Context getConcreteEndContext();
}
