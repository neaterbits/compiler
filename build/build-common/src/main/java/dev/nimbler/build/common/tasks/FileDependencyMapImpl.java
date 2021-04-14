package dev.nimbler.build.common.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import dev.nimbler.build.types.compile.FileDependencyMap;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.language.common.types.TypeName;

final class FileDependencyMapImpl implements FileDependencyMap {

	private final Map<TypeName, Set<SourceFileResourcePath>> filesDependingOn;
	
	FileDependencyMapImpl() {

		this.filesDependingOn = new HashMap<>();

	}
	
	@Override
	public Set<SourceFileResourcePath> getFilesDependingOn(TypeName sourceFile) {
		
		Objects.requireNonNull(sourceFile);
		
		return filesDependingOn.get(sourceFile);
	}

}
