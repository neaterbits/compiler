package com.neaterbits.compiler.codemap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.util.TypeName;

public final class NameToTypeNoMap {
	
	private final Map<TypeName, Integer> typesByScopedName;

	public NameToTypeNoMap() {
		this.typesByScopedName = new HashMap<>();
	}

	public void addMapping(TypeName name, int typeNo) {
		typesByScopedName.put(name, typeNo);
	}

	public Integer getType(TypeName typeName) {
		
		Objects.requireNonNull(typeName);

		return typesByScopedName.get(typeName);
	}

	@Override
	public String toString() {
		return typesByScopedName.toString();
	}
}
