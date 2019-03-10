package com.neaterbits.compiler.resolver.loader;

public interface FileInfo {

	default String getName() {
		return getSpec().getName();
	}

	FileSpec getSpec();

}
