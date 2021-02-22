package com.neaterbits.compiler.common.loader;

import java.util.Collection;

public interface CompiledFile extends FileInfo {
	
	Collection<CompiledType> getTypes();
	
	FileImports getImports();
}

