package com.neaterbits.compiler.resolver.types;

import java.util.Collection;

public interface CompiledFile<COMPLEXTYPE> extends FileInfo {
	
	Collection<CompiledType<COMPLEXTYPE>> getTypes();
	
	IFileImports getImports();
}

