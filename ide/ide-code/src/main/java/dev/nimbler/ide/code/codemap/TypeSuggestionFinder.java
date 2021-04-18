package dev.nimbler.ide.code.codemap;

import java.util.Map;

import dev.nimbler.ide.common.model.codemap.TypeSuggestion;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.typesources.TypeSources;

abstract class TypeSuggestionFinder {

	abstract boolean canRetrieveTypeVariant();
	
	abstract boolean hasSourceCode();
	
	abstract boolean findSuggestions(TypeNameMatcher matcher, Map<TypeName, TypeSuggestion> dst);
	
	abstract boolean hasType(TypeName typeName, TypeSources typeSources);
}
