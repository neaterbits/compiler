package com.neaterbits.compiler.util.modules;

import java.io.File;
import java.util.List;
import java.util.Objects;

public final class SourceModuleSpec extends ModuleSpec {
	
	private final File baseDirectory;

	public SourceModuleSpec(ModuleId moduleId, List<ModuleSpec> dependencies, File baseDirectory) {
		super(moduleId, dependencies);

		Objects.requireNonNull(baseDirectory);

		this.baseDirectory = baseDirectory;
	}

	public File getBaseDirectory() {
		return baseDirectory;
	}
}
