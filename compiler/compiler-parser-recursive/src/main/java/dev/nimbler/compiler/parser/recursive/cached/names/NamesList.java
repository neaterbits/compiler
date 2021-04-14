package dev.nimbler.compiler.parser.recursive.cached.names;

import dev.nimbler.compiler.parser.recursive.cached.ScratchList;
import dev.nimbler.compiler.util.name.Names;

public interface NamesList extends ScratchList<Names> {

    void add(int context, long name);
}
