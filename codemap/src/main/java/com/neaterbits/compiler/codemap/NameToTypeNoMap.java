package com.neaterbits.compiler.codemap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.build.types.ScopedName;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.build.types.TypesMap;

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
	
	public TypesMap<TypeName> makeTypesMap() {
	    
	    final Map<ScopedName, TypeName> map = new HashMap<>(typesByTypeName.size());
	    
	    for (TypeName typeName : typesByTypeName.keySet()) {
	        map.put(typeName.toScopedName(), typeName);
	    }
	    
	    return scopedName -> map.get(scopedName);
	}

	@Override
	public String toString() {
		return typesByTypeName.toString();
	}
}
