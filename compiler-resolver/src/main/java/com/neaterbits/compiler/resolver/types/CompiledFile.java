package com.neaterbits.compiler.resolver.types;

import java.util.Collection;

public interface CompiledFile<COMPLEXTYPE, COMPILATION_UNIT> extends FileInfo {
	
	Collection<CompiledType<COMPLEXTYPE>> getTypes();
	
	COMPILATION_UNIT getCompilationUnit();
	
}

