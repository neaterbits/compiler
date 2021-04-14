package com.neaterbits.ide.core.model.codemap;

import java.util.Map;

import com.neaterbits.ide.common.model.codemap.TypeSuggestion;
import com.neaterbits.language.common.types.TypeName;
import com.neaterbits.language.common.typesources.TypeSources;

abstract class TypeSuggestionFinder {

	abstract boolean canRetrieveTypeVariant();
	
	abstract boolean hasSourceCode();
	
	abstract boolean findSuggestions(TypeNameMatcher matcher, Map<TypeName, TypeSuggestion> dst);
	
	abstract boolean hasType(TypeName typeName, TypeSources typeSources);
}
