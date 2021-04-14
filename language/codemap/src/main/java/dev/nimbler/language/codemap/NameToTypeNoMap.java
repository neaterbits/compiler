package dev.nimbler.language.codemap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import dev.nimbler.language.common.types.ScopedName;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.types.TypesMap;

public final class NameToTypeNoMap {
	
	private final Map<TypeName, Integer> typesByTypeName;
	
	private TypeName [] byTypeNo;

	public NameToTypeNoMap() {
		this.typesByTypeName = new HashMap<>();
		this.byTypeNo = null;
	}

	public void addMapping(TypeName name, int typeNo) {

		typesByTypeName.put(name, typeNo);

		this.byTypeNo = ArrayAllocation.allocateArray(byTypeNo, typeNo, TypeName[]::new);
		byTypeNo[typeNo] = name;
	}

	public Integer getType(TypeName typeName) {
		
		Objects.requireNonNull(typeName);

		return typesByTypeName.get(typeName);
	}
	
	public TypeName getTypeName(int typeNo) {

	    return byTypeNo[typeNo];
	}
	
	public TypesMap<TypeName> makeTypesMap() {
	    
	    final Map<ScopedName, TypeName> map = new HashMap<>(typesByTypeName.size());
	    
	    for (TypeName typeName : typesByTypeName.keySet()) {
	        map.put(typeName.toScopedName(), typeName);
	    }

	    return new TypesMap<TypeName>() {

            @Override
            public TypeName lookupByScopedName(ScopedName scopedName) {
                return map.get(scopedName);
            }

            @Override
            public String toString() {
                return "NameToTypeNoMap.makeTypesMap() [ map=" 
                        + typesByTypeName + " ]";
            }
        };
	}

	@Override
	public String toString() {
		return typesByTypeName.toString();
	}
}
