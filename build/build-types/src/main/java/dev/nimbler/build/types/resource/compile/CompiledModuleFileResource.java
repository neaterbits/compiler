package dev.nimbler.build.types.resource.compile;

import java.io.File;

import dev.nimbler.build.types.resource.FileResource;

// References a compiled module file, eg. a jar file
public final class CompiledModuleFileResource extends FileResource {

	public CompiledModuleFileResource(File file) {
		super(file);
	}
}
