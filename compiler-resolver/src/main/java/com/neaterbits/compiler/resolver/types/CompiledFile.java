package com.neaterbits.compiler.resolver.types;

import java.util.Collection;

public interface CompiledFile extends FileInfo {
	
	Collection<CompiledType> getTypes();
	
	IFileImports getImports();
}

