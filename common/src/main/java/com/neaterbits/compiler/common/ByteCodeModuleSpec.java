package com.neaterbits.compiler.common;

import java.io.File;
import java.util.List;
import java.util.Objects;

public final class ByteCodeModuleSpec extends ModuleSpec {

	private final File byteCodeFile;

	public ByteCodeModuleSpec(ModuleId moduleId, List<ModuleSpec> dependencies, File byteCodeFile) {
		super(moduleId, dependencies);

		Objects.requireNonNull(byteCodeFile);

		this.byteCodeFile = byteCodeFile;
	}

	public File getByteCodeFile() {
		return byteCodeFile;
	}
}
