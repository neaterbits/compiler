package com.neaterbits.compiler.parser.recursive.cached.names;

import com.neaterbits.compiler.parser.recursive.cached.ScratchBuf;
import com.neaterbits.compiler.parser.recursive.cached.ScratchEntity;
import com.neaterbits.compiler.util.name.Names;
import com.neaterbits.compiler.util.parse.NamePart;

public final class NamesImpl
        extends ScratchEntity<NamePart, Names, NamesList>
        implements Names, NamesList {
    
    public NamesImpl(ScratchBuf<NamePart, Names, NamesList, ?> buf) {
        super(buf);
    }

    @Override
    public long getStringAt(int index) {
        return get(index).getPart();
    }

    @Override
    public int getContextAt(int index) {
        return get(index).getContext();
    }

    @Override
    public int count() {
        return getCount();
    }
    
    @Override
    protected Names getToProcess() {
        return this;
    }

    @Override
    protected NamesList getList() {
        return this;
    }

    @Override
    protected NamePart createPart() {
        return new NamePart();
    }

    @Override
    public void add(int context, long name) {

        getOrCreate().init(context, name);
    }
}
