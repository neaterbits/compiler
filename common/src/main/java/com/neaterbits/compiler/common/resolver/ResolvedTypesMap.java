package com.neaterbits.compiler.common.resolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.type.FullTypeName;
import com.neaterbits.compiler.common.loader.ResolvedType;

public final class ResolvedTypesMap {

	private final Map<FullTypeName, ResolvedType> map;
	
	ResolvedTypesMap() {
		this.map = new HashMap<>();
	}
	
	
	void addMapping(FullTypeName typeName, ResolvedType type) {
		Objects.requireNonNull(typeName);
		Objects.requireNonNull(type);
		
		map.put(typeName, type);
	}
	
	public ResolvedType lookupType(FullTypeName fullTypeName) {
		Objects.requireNonNull(fullTypeName);
		
		return map.get(fullTypeName);
	}
}
