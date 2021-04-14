package dev.nimbler.build.model;

import java.io.File;
import java.util.Objects;

import dev.nimbler.build.types.dependencies.DependencyType;
import dev.nimbler.build.types.resource.ModuleResourcePath;
import dev.nimbler.build.types.resource.compile.CompiledModuleFileResourcePath;

final class BuildDependency<DEPENDENCY> extends BaseDependency {

	private final DEPENDENCY dependency;
	
	BuildDependency(ModuleResourcePath resourcePath, DependencyType type, CompiledModuleFileResourcePath compiledModuleFileResourcePath, File compiledModuleFile, DEPENDENCY dependency) {
		super(resourcePath, type, compiledModuleFileResourcePath, compiledModuleFile);

		Objects.requireNonNull(dependency);

		this.dependency = dependency;
	}

	DEPENDENCY getDependency() {
		return dependency;
	}
}
