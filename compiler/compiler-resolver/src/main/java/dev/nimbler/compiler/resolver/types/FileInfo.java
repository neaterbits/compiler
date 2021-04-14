package dev.nimbler.compiler.resolver.types;

import dev.nimbler.compiler.util.FileSpec;

public interface FileInfo {

	default String getName() {
		return getSpec().getParseContextName();
	}

	FileSpec getSpec();

}
