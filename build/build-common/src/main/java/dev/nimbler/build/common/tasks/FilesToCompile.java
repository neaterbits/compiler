package dev.nimbler.build.common.tasks;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import dev.nimbler.build.types.resource.SourceFileResourcePath;

final class FilesToCompile {

	private final List<SourceFileResourcePath> toCompile;
	private final List<SourceFileResourcePath> alreadyBuilt;

	FilesToCompile(List<SourceFileResourcePath> toCompile, List<SourceFileResourcePath> alreadyBuilt) {
		
		Objects.requireNonNull(toCompile);
		Objects.requireNonNull(alreadyBuilt);
		
		this.toCompile = toCompile;
		this.alreadyBuilt = alreadyBuilt;
	}

	List<SourceFileResourcePath> getToCompile() {
		return Collections.unmodifiableList(toCompile);
	}

	List<SourceFileResourcePath> getAlreadyBuilt() {
		return Collections.unmodifiableList(alreadyBuilt);
	}
}
