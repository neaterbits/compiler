package com.neaterbits.compiler.resolver.passes;

import java.util.function.Function;

import com.neaterbits.compiler.util.ScopedName;

public interface LibraryTypes<LIBRARYTYPE> extends Function<ScopedName, LIBRARYTYPE> {

}
