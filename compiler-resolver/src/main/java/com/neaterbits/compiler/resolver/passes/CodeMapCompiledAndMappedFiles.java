package com.neaterbits.compiler.resolver.passes;

import com.neaterbits.compiler.codemap.compiler.CompilerCodeMapGetters;
import com.neaterbits.compiler.codemap.compiler.CrossReferenceUpdater;
import com.neaterbits.compiler.model.common.CompiledAndMappedFiles;
import com.neaterbits.compiler.util.FileSpec;

public interface CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> extends CompiledAndMappedFiles {

	CompilerCodeMapGetters getCompilerCodeMap();
	
	CrossReferenceUpdater getCrossReferenceUpdater();
	
	COMPILATION_UNIT getCompilationUnit(FileSpec fileSpec);
	
}
