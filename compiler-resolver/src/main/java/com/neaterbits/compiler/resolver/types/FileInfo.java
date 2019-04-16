package com.neaterbits.compiler.resolver.types;

import com.neaterbits.compiler.util.FileSpec;

public interface FileInfo {

	default String getName() {
		return getSpec().getParseContextName();
	}

	FileSpec getSpec();

}
