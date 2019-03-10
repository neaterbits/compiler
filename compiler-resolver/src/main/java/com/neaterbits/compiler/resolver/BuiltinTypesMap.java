package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.util.ScopedName;

final class BuiltinTypesMap {

	private final Map<ScopedName, BuiltinType> byScopedName;
	private final Map<String, BuiltinType> byName;
	
	BuiltinTypesMap(Collection<? extends BuiltinType> builtinTypes) {

		this.byScopedName = new HashMap<>(builtinTypes.size());
		this.byName = new HashMap<>(builtinTypes.size());

		for (BuiltinType builtinType : builtinTypes) {
			byScopedName.put(builtinType.getCompleteName().toScopedName(), builtinType);
			
			byName.put(builtinType.getName().getName(), builtinType);
		}
	}


	BuiltinType lookupType(ScopedName typeName) {
		
		Objects.requireNonNull(typeName);
		
		final BuiltinType type;
		
		if (typeName.hasScope()) {
			type = byScopedName.get(typeName);
		}
		else {
			type = byName.get(typeName.getName());
		}
		
		return type;
	}
	
}
