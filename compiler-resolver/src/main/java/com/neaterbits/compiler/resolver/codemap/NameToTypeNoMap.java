package com.neaterbits.compiler.resolver.codemap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.ast.type.CompleteName;

final class NameToTypeNoMap {
	
	private final Map<CompleteName, Integer> typesByScopedName;

	NameToTypeNoMap() {
		this.typesByScopedName = new HashMap<>();
	}

	void addMapping(CompleteName name, int typeNo) {
		typesByScopedName.put(name, typeNo);
	}

	Integer getType(CompleteName typeName) {
		
		Objects.requireNonNull(typeName);

		return typesByScopedName.get(typeName);
	}

	@Override
	public String toString() {
		return typesByScopedName.toString();
	}
}
