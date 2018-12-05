package com.neaterbits.compiler.bytecode.common.loader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neaterbits.compiler.bytecode.common.ClassBytecode;
import com.neaterbits.compiler.bytecode.common.TypeMap;
import com.neaterbits.compiler.common.TypeName;
import com.neaterbits.compiler.common.resolver.codemap.CodeMap;
import com.neaterbits.compiler.common.resolver.codemap.CodeMap.TypeResult;

final class HashTypeMap implements TypeMap {

	@FunctionalInterface
	interface LoadType {
		ClassBytecode load(TypeName typeName);
	}
	
	private final Map<TypeName, Integer> typeByName;

	HashTypeMap() {
		this.typeByName = new HashMap<>();
	}

	@Override
	public int getType(TypeName typeName) {
		final Integer type;
		
		synchronized (this) {
			type = typeByName.get(typeName);
		}
		
		return type != null ? type : -1;
	}
	
	ClassBytecode addOrGetType(TypeName typeName, CodeMap codeMap, TypeResult typeResult, LoadType loadType) {
		
		Integer type;
		
		synchronized (this) {
			type = typeByName.get(typeName);
		}
		
		final ClassBytecode addedBytecode;
		
		if (type == null) {
			
			final ClassBytecode classBytecode = loadType.load(typeName);
			
			if (classBytecode != null) {
				
				final List<TypeName> extendsFrom = Arrays.asList(classBytecode.getSuperClass());
				
				final int [] extendsFromTypes = new int[extendsFrom.size()];
				
				for (int i = 0; i < extendsFromTypes.length; ++ i) {
					final int extendsFromType = typeByName.get(extendsFrom.get(i));
					
					extendsFromTypes[i] = extendsFromType;
				}

				synchronized (this) {
					type = typeByName.get(typeName);
					
					if (type == null) {
						// Type added by other thread
						addedBytecode = null;
					}
					else {
						type = codeMap.addType(classBytecode.getTypeVariant(), extendsFromTypes);

						typeByName.put(typeName, type);

						addedBytecode = classBytecode;
						
					}
				}
			}
			else {
				addedBytecode = null;
			}
		}
		else {
			addedBytecode = null;
		}
		
		typeResult.type = type != null ? type : -1;
		
		return addedBytecode;
	}
}
