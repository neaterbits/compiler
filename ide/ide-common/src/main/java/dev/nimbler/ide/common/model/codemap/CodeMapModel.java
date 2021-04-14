package dev.nimbler.ide.common.model.codemap;

import dev.nimbler.compiler.model.common.ResolvedTypes;

public interface CodeMapModel extends ResolvedTypes {

	TypeSuggestions findSuggestions(String searchText, boolean onlyTypesWithSourceCode);
	
}

