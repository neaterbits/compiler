package dev.nimbler.build.common;

import java.util.Objects;
import java.util.Set;

import dev.nimbler.build.types.resource.SourceFileResourcePath;

public final class SourceFiles {

	private final Set<SourceFileResourcePath> files;

	public SourceFiles(Set<SourceFileResourcePath> files) {
		
		Objects.requireNonNull(files);
		
		this.files = files;
	}

	public Set<SourceFileResourcePath> getFiles() {
		return files;
	}
}
