package com.neaterbits.compiler.parser.recursive.cached.types;

import com.neaterbits.compiler.parser.recursive.cached.names.NamesList;
import com.neaterbits.util.parse.context.Context;

public interface TypeArgument {

    public enum Type {

        REFERENCE,
        
        WILDCARD
    }
    
    Type getType();
    
    int getReferenceStartContext();
    
    // if isGenericTypeName() is false
    NamesList getReferenceTypeNames();
    
    TypeArgumentsList getReferenceTypeGenerics();
    
    Context getReferenceEndContext();
    
    int getWildcardStartContext();
    
    Context getWildcardEndContext();
}
