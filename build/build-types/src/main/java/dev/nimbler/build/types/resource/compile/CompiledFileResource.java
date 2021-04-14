package dev.nimbler.build.types.resource.compile;

import java.io.File;

import dev.nimbler.build.types.resource.FileResource;

// Compiled file, eg. .class file
public final class CompiledFileResource extends FileResource {

	public CompiledFileResource(File file) {
		super(file);
	}
}
