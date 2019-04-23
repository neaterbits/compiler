package com.neaterbits.compiler.resolver.passes;

import com.neaterbits.compiler.codemap.compiler.CompilerCodeMapGetters;
import com.neaterbits.compiler.codemap.compiler.CrossReferenceUpdater;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.model.CompiledAndMappedFiles;

public interface CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> extends CompiledAndMappedFiles {

	CompilerCodeMapGetters getCompilerCodeMap();
	
	CrossReferenceUpdater getCrossReferenceUpdater();
	
	COMPILATION_UNIT getCompilationUnit(FileSpec fileSpec);
	
}
