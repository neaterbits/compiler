package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.util.ScopedName;

final class CompiledTypesMap<COMPLEXTYPE> implements TypesMap<CompiledType<COMPLEXTYPE>> {

	private final Map<ScopedName, CompiledType<COMPLEXTYPE>> map;
	
	<COMPILATION_UNIT> CompiledTypesMap(Collection<CompiledFile<COMPLEXTYPE, COMPILATION_UNIT>> files) {
		
		this.map = new HashMap<>();

		for (CompiledFile<COMPLEXTYPE, COMPILATION_UNIT> file : files) {
			addTypes(file.getTypes());
		}
	}
	
	private void addTypes(Collection<CompiledType<COMPLEXTYPE>> types) {
		
		for (CompiledType<COMPLEXTYPE> type : types) {
			
			map.put(type.getScopedName(), type);
			
			if (type.getNestedTypes() != null) {
				addTypes(type.getNestedTypes());
			}
		}
	}
	
	@Override
	public CompiledType<COMPLEXTYPE> lookupByScopedName(ScopedName scopedName) {
		
		Objects.requireNonNull(scopedName);
		
		return map.get(scopedName);
	}
}
