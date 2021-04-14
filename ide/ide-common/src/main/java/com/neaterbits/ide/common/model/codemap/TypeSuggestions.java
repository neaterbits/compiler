package com.neaterbits.ide.common.model.codemap;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class TypeSuggestions {

	private final List<TypeSuggestion> typeSuggestions;
	private final boolean completeResult;

	public TypeSuggestions(List<TypeSuggestion> typeSuggestions, boolean completeResult) {
	
		Objects.requireNonNull(typeSuggestions);
		
		this.typeSuggestions = Collections.unmodifiableList(typeSuggestions);
		this.completeResult = completeResult;
	}

	public List<TypeSuggestion> getTypeSuggestions() {
		return typeSuggestions;
	}

	public boolean isCompleteResult() {
		return completeResult;
	}
}
