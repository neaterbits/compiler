package com.neaterbits.compiler.codemap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.util.TypeName;

public final class NameToTypeNoMap {
	
	private final Map<TypeName, Integer> typesByTypeName;

	public NameToTypeNoMap() {
		this.typesByTypeName = new HashMap<>();
	}

	public void addMapping(TypeName name, int typeNo) {
		typesByTypeName.put(name, typeNo);
	}

	public Integer getType(TypeName typeName) {
		
		Objects.requireNonNull(typeName);

		return typesByTypeName.get(typeName);
	}

	@Override
	public String toString() {
		return typesByTypeName.toString();
	}
}
