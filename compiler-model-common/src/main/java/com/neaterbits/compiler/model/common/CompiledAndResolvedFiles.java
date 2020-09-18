package com.neaterbits.compiler.model.common;

import java.util.List;

import com.neaterbits.compiler.util.FileSpec;

public interface CompiledAndResolvedFiles {

	CompiledAndResolvedFile getFile(FileSpec fileSpec);

	List<CompiledAndResolvedFile> getFiles();
	
}
