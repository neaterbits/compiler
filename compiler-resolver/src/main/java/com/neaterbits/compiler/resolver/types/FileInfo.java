package com.neaterbits.compiler.resolver.types;

public interface FileInfo {

	default String getName() {
		return getSpec().getName();
	}

	FileSpec getSpec();

}
