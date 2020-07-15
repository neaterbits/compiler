package com.neaterbits.compiler.parser.recursive.cached.types;

import com.neaterbits.compiler.parser.recursive.cached.ScratchList;
import com.neaterbits.compiler.parser.recursive.cached.names.NamesList;
import com.neaterbits.compiler.util.Context;

public interface TypeArgumentsList extends ScratchList<TypeArguments> {

    void addGenericType(int context, long name);

    void addConcreteType(NamesList names, TypeArgumentsList typeArguments, Context endContext);

    void setContexts(int startContext, Context endContext);
}
