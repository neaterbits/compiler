package com.neaterbits.compiler.common.resolver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.loader.CompiledFile;
import com.neaterbits.compiler.common.loader.CompiledType;

final class CompiledTypesMap implements TypesMap<CompiledType> {

	private final Map<ScopedName, CompiledType> map;
	
	CompiledTypesMap(Collection<CompiledFile> files) {
		
		this.map = new HashMap<>();

		for (CompiledFile file : files) {
			addTypes(file.getTypes());
		}
	}
	
	private void addTypes(Collection<CompiledType> types) {
		
		for (CompiledType type : types) {
			
			map.put(type.getScopedName(), type);
			
			if (type.getNestedTypes() != null) {
				addTypes(type.getNestedTypes());
			}
		}
	}
	
	@Override
	public CompiledType lookupByScopedName(ScopedName scopedName) {
		
		Objects.requireNonNull(scopedName);
		
		return map.get(scopedName);
	}
}
