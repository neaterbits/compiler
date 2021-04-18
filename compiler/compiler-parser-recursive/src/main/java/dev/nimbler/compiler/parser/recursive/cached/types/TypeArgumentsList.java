package dev.nimbler.compiler.parser.recursive.cached.types;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.parser.recursive.cached.ScratchList;
import dev.nimbler.compiler.parser.recursive.cached.names.NamesList;

public interface TypeArgumentsList extends ScratchList<TypeArguments> {

    void addReferenceType(int startContext, NamesList names, TypeArgumentsList typeArguments, Context endContext);
    
    void addWildcardType(int startContext, Context endContext);

    void setContexts(int startContext, Context endContext);
}
