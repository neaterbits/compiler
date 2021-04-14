package dev.nimbler.ide.common.model.codemap;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.language.common.types.TypeVariant;

public interface TypeSuggestion {

	TypeVariant getType();

	String getNamespace();

	String getName();

	String getBinaryName(); // eg. SomeType.class
	
	SourceFileResourcePath getSourceFile();

	default boolean isSourceFile() {
		return getSourceFile() != null;
	}
}
