package com.neaterbits.compiler.parser.recursive;

import com.neaterbits.compiler.util.name.Names;
import com.neaterbits.compiler.util.parse.NamePart;

final class NamesImpl
        extends ScratchEntity<NamePart, Names, NamesList>
        implements Names, NamesList {
    
    NamesImpl(ScratchBuf<NamePart, Names, NamesList, ?> buf) {
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
    Names getToProcess() {
        return this;
    }

    @Override
    NamesList getList() {
        return this;
    }

    @Override
    NamePart createPart() {
        return new NamePart();
    }

    @Override
    public void add(int context, long name) {

        getOrCreate().init(context, name);
    }
}
