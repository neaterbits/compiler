package com.neaterbits.compiler.common.loader;

public interface FileInfo {

	default String getName() {
		return getSpec().getName();
	}

	FileSpec getSpec();

}
