package dev.nimbler.compiler.resolver.passes;

import java.util.function.Function;

import dev.nimbler.compiler.model.common.LibraryTypeRef;
import dev.nimbler.language.common.types.ScopedName;

public interface LibraryTypes extends Function<ScopedName, LibraryTypeRef> {

}
