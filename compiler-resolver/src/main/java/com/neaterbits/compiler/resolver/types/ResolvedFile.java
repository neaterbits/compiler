package com.neaterbits.compiler.resolver.types;

import java.util.Collection;

public interface ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> extends FileInfo {

	Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> getTypes();
	
}
