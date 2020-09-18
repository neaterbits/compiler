package com.neaterbits.compiler.resolver.passes;

import java.util.function.Function;

import com.neaterbits.compiler.model.common.LibraryTypeRef;
import com.neaterbits.compiler.util.ScopedName;

public interface LibraryTypes extends Function<ScopedName, LibraryTypeRef> {

}
