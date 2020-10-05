package com.neaterbits.compiler.parser.recursive.cached.types;

import com.neaterbits.compiler.parser.recursive.cached.ScratchList;
import com.neaterbits.compiler.parser.recursive.cached.names.NamesList;
import com.neaterbits.util.parse.context.Context;

public interface TypeArgumentsList extends ScratchList<TypeArguments> {

    void addReferenceType(int startContext, NamesList names, TypeArgumentsList typeArguments, Context endContext);
    
    void addWildcardType(int startContext, Context endContext);

    void setContexts(int startContext, Context endContext);
}
