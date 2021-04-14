package dev.nimbler.compiler.language.java.compile;

import dev.nimbler.compiler.language.java.TestFile;

interface CompiledAndResolvedFiles {

	CompiledAndResolvedFile getFile(String name);
	
	default CompiledAndResolvedFile getFile(TestFile testFile) {
	    
	    return getFile(testFile.getName());
	}

    /*
	List<CompiledAndResolvedFile> getFiles();
	*/
	
}
