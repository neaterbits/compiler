package com.neaterbits.compiler.resolver.loader;

import java.util.Collection;

public interface Loader {

	Collection<CompiledFile> getAllFiles();
	
	CompiledFile load(LoadSpec loadSpec);
	
}
