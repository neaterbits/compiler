package dev.nimbler.ide.code.codemap;

import java.util.function.Function;

import dev.nimbler.build.model.runtimeenvironment.RuntimeEnvironment;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;

public interface Projects {

	RuntimeEnvironment getRuntimeEnvironment(ProjectModuleResourcePath module);

	<T> T forEachSourceFolder(Function<SourceFolderResourcePath, T> function);
}
