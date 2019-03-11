package com.neaterbits.compiler.resolver.types;

import java.util.Collection;

public interface ResolvedFile<BUILTINTYPE, COMPLEXTYPE> extends FileInfo {

	Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> getTypes();
	
}
