package com.neaterbits.compiler.parser.recursive;

import com.neaterbits.compiler.util.name.Names;

public interface NamesList extends ScratchList<Names> {

    void add(int context, long name);
}
