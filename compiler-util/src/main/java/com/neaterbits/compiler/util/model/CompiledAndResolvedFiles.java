package com.neaterbits.compiler.util.model;

import java.util.List;

import com.neaterbits.compiler.util.FileSpec;

public interface CompiledAndResolvedFiles {

	CompiledAndResolvedFile getFile(FileSpec fileSpec);

	List<CompiledAndResolvedFile> getFiles();
	
}
