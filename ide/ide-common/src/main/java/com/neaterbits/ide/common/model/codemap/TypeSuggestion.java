package com.neaterbits.ide.common.model.codemap;

import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.compiler.types.TypeVariant;

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
