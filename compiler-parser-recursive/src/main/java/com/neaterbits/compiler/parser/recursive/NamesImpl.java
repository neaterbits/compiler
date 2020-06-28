package com.neaterbits.compiler.parser.recursive;

import com.neaterbits.compiler.util.name.Names;
import com.neaterbits.compiler.util.parse.NamePart;

final class NamesImpl extends ScratchEntity<NamePart, Names> implements Names {
    
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

    void add(int context, long name) {
        
        super.add(() -> new NamePart(context, name), part -> part.init(context, name));
    }
}
