package com.neaterbits.compiler.resolver.types;

import java.util.Collection;

public interface CompiledFile<COMPILATION_UNIT> extends FileInfo {
	
	Collection<CompiledType> getTypes();
	
	COMPILATION_UNIT getCompilationUnit();
	
}

