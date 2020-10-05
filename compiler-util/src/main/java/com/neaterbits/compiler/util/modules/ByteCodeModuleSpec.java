package com.neaterbits.compiler.util.modules;

import java.io.File;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.types.ModuleId;

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

	@Override
	public File getBaseDirectory() {
		return byteCodeFile;
	}
}
