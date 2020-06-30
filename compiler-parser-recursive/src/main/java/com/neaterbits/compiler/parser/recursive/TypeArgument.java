package com.neaterbits.compiler.parser.recursive;

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
