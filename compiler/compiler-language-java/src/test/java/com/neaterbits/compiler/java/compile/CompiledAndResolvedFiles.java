package com.neaterbits.compiler.java.compile;

import com.neaterbits.compiler.java.TestFile;

interface CompiledAndResolvedFiles {

	CompiledAndResolvedFile getFile(String name);
	
	default CompiledAndResolvedFile getFile(TestFile testFile) {
	    
	    return getFile(testFile.getName());
	}

    /*
	List<CompiledAndResolvedFile> getFiles();
	*/
	
}
