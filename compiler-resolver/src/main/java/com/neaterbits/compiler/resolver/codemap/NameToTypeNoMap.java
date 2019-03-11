package com.neaterbits.compiler.resolver.codemap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.util.TypeName;

final class NameToTypeNoMap {
	
	private final Map<TypeName, Integer> typesByTypeName;

	NameToTypeNoMap() {
		this.typesByTypeName = new HashMap<>();
	}

	void addMapping(TypeName name, int typeNo) {
		typesByTypeName.put(name, typeNo);
	}

	Integer getType(TypeName typeName) {
		
		Objects.requireNonNull(typeName);

		return typesByTypeName.get(typeName);
	}

	@Override
	public String toString() {
		return typesByTypeName.toString();
	}
}
