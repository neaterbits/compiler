package com.neaterbits.compiler.common.resolver.codemap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.type.FullTypeName;

final class NameToTypeNoMap {
	
	private final Map<FullTypeName, Integer> typesByScopedName;

	NameToTypeNoMap() {
		this.typesByScopedName = new HashMap<>();
	}

	void addMapping(FullTypeName name, int typeNo) {
		typesByScopedName.put(name, typeNo);
	}

	Integer getType(FullTypeName typeName) {
		
		Objects.requireNonNull(typeName);

		return typesByScopedName.get(typeName);
	}
}
