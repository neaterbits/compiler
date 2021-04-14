package dev.nimbler.build.types.compile;

import java.util.Set;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.language.common.types.TypeName;

public interface FileDependencyMap {

	Set<SourceFileResourcePath> getFilesDependingOn(TypeName typeName);
	
}
