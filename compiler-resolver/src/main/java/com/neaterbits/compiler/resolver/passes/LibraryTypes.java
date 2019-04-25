package com.neaterbits.compiler.resolver.passes;

import java.util.function.Function;

import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.model.LibraryTypeRef;

public interface LibraryTypes extends Function<ScopedName, LibraryTypeRef> {

}
