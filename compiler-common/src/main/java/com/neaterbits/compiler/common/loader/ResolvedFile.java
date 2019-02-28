package com.neaterbits.compiler.common.loader;

import java.util.Collection;

public interface ResolvedFile extends FileInfo {

	Collection<ResolvedType> getTypes();
	
}
