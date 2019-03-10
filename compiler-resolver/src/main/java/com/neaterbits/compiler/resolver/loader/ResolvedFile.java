package com.neaterbits.compiler.resolver.loader;

import java.util.Collection;

public interface ResolvedFile extends FileInfo {

	Collection<ResolvedType> getTypes();
	
}
