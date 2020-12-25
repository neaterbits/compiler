package com.neaterbits.compiler.java;

import com.neaterbits.compiler.util.FileSpec;

public interface CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> extends CompiledAndMappedFiles {

    /*
	CompilerCodeMapGetters getCompilerCodeMap();
	
	CrossReferenceUpdater getCrossReferenceUpdater();
    */
	
	COMPILATION_UNIT getCompilationUnit(FileSpec fileSpec);
}
