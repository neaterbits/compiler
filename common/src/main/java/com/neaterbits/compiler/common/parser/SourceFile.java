package com.neaterbits.compiler.common.parser;

import java.io.File;
import java.util.Objects;

public final class SourceFile {

	private final File path;

	public SourceFile(File path) {
		
		Objects.requireNonNull(path);
		
		this.path = path;
	}

	public File getPath() {
		return path;
	}
}
