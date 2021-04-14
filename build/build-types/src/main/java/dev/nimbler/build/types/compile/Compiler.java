package dev.nimbler.build.types.compile;

import java.io.File;
import java.io.IOException;
import java.util.List;

import dev.nimbler.build.types.resource.SourceFileResourcePath;

public interface Compiler {

	boolean supportsCompilingMultipleFiles();
	
	CompilerStatus compile(
	        List<SourceFileResourcePath> sourceFiles,
	        File targetDirectory,
	        List<File> compiledDependencies) throws IOException;
	
}
