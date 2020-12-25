package com.neaterbits.compiler.java;

import com.neaterbits.compiler.util.FileSpec;

interface CompiledAndResolvedFiles {

	CompiledAndResolvedFile getFile(FileSpec fileSpec);

    /*
	List<CompiledAndResolvedFile> getFiles();
	*/
	
}
