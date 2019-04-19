package com.neaterbits.compiler.util.model;

import com.neaterbits.compiler.util.FileSpec;

public interface CompiledAndResolvedFiles {

	CompiledAndResolvedFile getFile(FileSpec fileSpec);

}
