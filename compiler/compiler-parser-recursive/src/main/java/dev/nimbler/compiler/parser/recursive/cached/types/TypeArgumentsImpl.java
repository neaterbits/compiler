package dev.nimbler.compiler.parser.recursive.cached.types;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.parser.recursive.cached.ScratchBuf;
import dev.nimbler.compiler.parser.recursive.cached.ScratchEntity;
import dev.nimbler.compiler.parser.recursive.cached.names.NamesList;

public final class TypeArgumentsImpl
    extends ScratchEntity<TypeArgumentImpl, TypeArguments, TypeArgumentsList> 
    implements TypeArguments, TypeArgumentsList {

    private int startContext;
    private Context endContext;
    
    public TypeArgumentsImpl(ScratchBuf<TypeArgumentImpl, TypeArguments, TypeArgumentsList, ?> buf) {
        super(buf);
    }

    @Override
    protected TypeArgumentImpl createPart() {
        return new TypeArgumentImpl();
    }

    @Override
    public void addReferenceType(int startContext, NamesList names, TypeArgumentsList typeArguments, Context endContext) {

        getOrCreate().initReferenceType(startContext, names, typeArguments, endContext);
    }

    @Override
    public void addWildcardType(int startContext, Context endContext) {

        getOrCreate().initWildcardType(startContext, endContext);
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
    protected TypeArguments getToProcess() {

        return this;
    }

    @Override
    protected TypeArgumentsList getList() {
        return this;
    }
}
