package dev.nimbler.build.common.language;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.build.types.resource.compile.CompiledFileResourcePath;
import dev.nimbler.build.types.resource.compile.TargetDirectoryResourcePath;

public interface BuildableLanguage {

	CompiledFileResourcePath getCompiledFilePath(TargetDirectoryResourcePath targetDirectory, SourceFileResourcePath sourceFile);

}
