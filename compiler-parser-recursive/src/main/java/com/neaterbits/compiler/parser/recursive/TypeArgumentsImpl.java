package com.neaterbits.compiler.parser.recursive;

import java.util.Objects;

import com.neaterbits.compiler.util.Context;

final class TypeArgumentsImpl
    extends ScratchEntity<TypeArgumentImpl, TypeArguments, TypeArgumentsList> 
    implements TypeArguments, TypeArgumentsList {

    private int startContext;
    private Context endContext;
    
    TypeArgumentsImpl(ScratchBuf<TypeArgumentImpl, TypeArguments, TypeArgumentsList, ?> buf) {
        super(buf);
    }

    @Override
    TypeArgumentImpl createPart() {
        return new TypeArgumentImpl();
    }

    @Override
    public void addGenericType(int context, long name) {

        getOrCreate().init(context, name);
    }

    @Override
    public void addConcreteType(NamesList names, TypeArgumentsList typeArguments, Context endContext) {

        getOrCreate().init(names, typeArguments, endContext);
    }

    @Override
    public int getStartContext() {
        return startContext;
    }

    @Override
    public Context getEndContext() {
        return endContext;
    }

    @Override
    public void setContexts(int startContext, Context endContext) {

        Objects.requireNonNull(endContext);
        
        this.startContext = startContext;
        this.endContext = endContext;
    }

    @Override
    public TypeArgument getTypeArgument(int index) {

        return get(index);
    }

    @Override
    public int count() {

        return getCount();
    }

    @Override
    TypeArguments getToProcess() {

        return this;
    }

    @Override
    TypeArgumentsList getList() {
        return this;
    }
}
