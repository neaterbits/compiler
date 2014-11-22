package com.neaterbits.compiler.common.resolver.codemap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.type.CompleteName;

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
}
