package dev.nimbler.build.types.resource;

import java.io.File;

public abstract class SourceFileHolderResource extends FileSystemResource {

	SourceFileHolderResource(File file, String name) {
		super(file, name);
	}
}
