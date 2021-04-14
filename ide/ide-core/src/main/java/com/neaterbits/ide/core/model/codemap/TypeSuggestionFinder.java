package com.neaterbits.ide.core.model.codemap;

import java.util.Map;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.util.model.TypeSources;
import com.neaterbits.ide.common.model.codemap.TypeSuggestion;

abstract class TypeSuggestionFinder {

	abstract boolean canRetrieveTypeVariant();
	
	abstract boolean hasSourceCode();
	
	abstract boolean findSuggestions(TypeNameMatcher matcher, Map<TypeName, TypeSuggestion> dst);
	
	abstract boolean hasType(TypeName typeName, TypeSources typeSources);
}
