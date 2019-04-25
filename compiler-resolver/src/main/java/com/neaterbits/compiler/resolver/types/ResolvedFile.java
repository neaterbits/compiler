package com.neaterbits.compiler.resolver.types;

import java.util.Collection;

public interface ResolvedFile extends FileInfo {

	Collection<ResolvedType> getTypes();
	
}
