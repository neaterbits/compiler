package com.neaterbits.compiler.java;

interface CompiledAndResolvedFiles {

	CompiledAndResolvedFile getFile(String name);
	
	default CompiledAndResolvedFile getFile(TestFile testFile) {
	    
	    return getFile(testFile.getName());
	}

    /*
	List<CompiledAndResolvedFile> getFiles();
	*/
	
}
