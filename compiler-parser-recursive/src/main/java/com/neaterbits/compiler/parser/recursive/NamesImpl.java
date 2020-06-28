package com.neaterbits.compiler.parser.recursive;

import java.util.ArrayList;
import java.util.List;

import com.neaterbits.compiler.util.name.Names;
import com.neaterbits.compiler.util.parse.NamePart;

final class NamesImpl implements Names {
    
    private final List<NamePart> namePartList;
    private int namePartElements;
    private boolean inUse;
    
    NamesImpl(boolean inUse) {
        // Allocate 100 elements, should always be enough or will cause exception
        this.namePartList = new ArrayList<>(100);
        this.namePartElements = 0;
        this.inUse = inUse;
    }
    
    @Override
    public long getStringAt(int index) {
        return namePartList.get(index).getPart();
    }

    @Override
    public int getContextAt(int index) {
        return namePartList.get(index).getContext();
    }

    @Override
    public int count() {
        return namePartElements;
    }

    boolean isInUse() {
        return inUse;
    }

    void setInUse(boolean inUse) {
        this.inUse = inUse;
    }
    
    void add(int context, long name) {

        if (namePartElements == namePartList.size()) {
            
            final NamePart namePart = new NamePart(context, name);

            namePartList.add(namePart);
        }
        else {
            namePartList.get(namePartElements).init(context, name);
        }

        ++ namePartElements;
    }
    
    void clear() {
        this.namePartElements = 0;
        this.inUse = false;
    }
}
