package com.neaterbits.compiler.common.resolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.loader.ResolvedType;

public final class ResolvedTypesMap {

	private final Map<CompleteName, ResolvedType> map;
	
	ResolvedTypesMap() {
		this.map = new HashMap<>();
	}
	
	
	void addMapping(CompleteName typeName, ResolvedType type) {
		Objects.requireNonNull(typeName);
		Objects.requireNonNull(type);
		
		map.put(typeName, type);
	}
	
	public ResolvedType lookupType(CompleteName completeName) {
		Objects.requireNonNull(completeName);
		
		return map.get(completeName);
	}
}
