package dev.nimbler.build.types.dependencies;

import java.io.File;

import dev.nimbler.build.types.resource.ModuleResourcePath;
import dev.nimbler.build.types.resource.compile.CompiledModuleFileResourcePath;

public interface ModuleDependency<MODULE extends ModuleResourcePath> extends Dependency {

	MODULE getModulePath();

	CompiledModuleFileResourcePath getCompiledModuleFilePath();

	File getCompiledModuleFile();
}
