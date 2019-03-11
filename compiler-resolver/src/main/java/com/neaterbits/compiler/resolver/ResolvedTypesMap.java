package com.neaterbits.compiler.resolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.util.TypeName;

public final class ResolvedTypesMap<BUILTINTYPE, COMPLEXTYPE> {

	private final Map<TypeName, ResolvedType<BUILTINTYPE, COMPLEXTYPE>> map;
	
	ResolvedTypesMap() {
		this.map = new HashMap<>();
	}
	
	
	void addMapping(TypeName typeName, ResolvedType<BUILTINTYPE, COMPLEXTYPE> type) {
		Objects.requireNonNull(typeName);
		Objects.requireNonNull(type);
		
		map.put(typeName, type);
	}
	
	public ResolvedType<BUILTINTYPE, COMPLEXTYPE> lookupType(TypeName completeName) {
		Objects.requireNonNull(completeName);
		
		return map.get(completeName);
	}
}
