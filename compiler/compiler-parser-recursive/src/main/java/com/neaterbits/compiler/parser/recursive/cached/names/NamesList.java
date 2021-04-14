package com.neaterbits.compiler.parser.recursive.cached.names;

import com.neaterbits.compiler.parser.recursive.cached.ScratchList;
import com.neaterbits.compiler.util.name.Names;

public interface NamesList extends ScratchList<Names> {

    void add(int context, long name);
}
