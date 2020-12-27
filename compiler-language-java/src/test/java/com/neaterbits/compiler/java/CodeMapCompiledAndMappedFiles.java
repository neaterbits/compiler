package com.neaterbits.compiler.java;

public interface CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> extends CompiledAndMappedFiles {

    /*
	CompilerCodeMapGetters getCompilerCodeMap();
	
	CrossReferenceUpdater getCrossReferenceUpdater();
    */
	
 	COMPILATION_UNIT getCompilationUnit(String name);

 	default COMPILATION_UNIT getCompilationUnit(TestFile testFile) {
 	    
 	    return getCompilationUnit(testFile.getName());
 	}
}
