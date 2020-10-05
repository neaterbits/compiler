package com.neaterbits.compiler.resolver.passes;

import java.util.function.Function;

import com.neaterbits.build.types.ScopedName;
import com.neaterbits.compiler.model.common.LibraryTypeRef;

public interface LibraryTypes extends Function<ScopedName, LibraryTypeRef> {

}
