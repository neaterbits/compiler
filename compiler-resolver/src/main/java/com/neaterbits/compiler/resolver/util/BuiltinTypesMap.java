package com.neaterbits.compiler.resolver.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.model.common.BuiltinTypeRef;
import com.neaterbits.compiler.util.ScopedName;

public final class BuiltinTypesMap {

	private final Map<ScopedName, BuiltinTypeRef> byScopedName;
	private final Map<String, BuiltinTypeRef> byName;
	
	public BuiltinTypesMap(Collection<BuiltinTypeRef> builtinTypes) {

		this.byScopedName = new HashMap<>(builtinTypes.size());
		this.byName = new HashMap<>(builtinTypes.size());

		for (BuiltinTypeRef builtinType : builtinTypes) {
			byScopedName.put(builtinType.toScopedName(), builtinType);
			
			byName.put(builtinType.getNameString(), builtinType);
		}
	}


	public BuiltinTypeRef lookupType(ScopedName typeName) {
		
		Objects.requireNonNull(typeName);
		
		final BuiltinTypeRef type;
		
		if (typeName.hasScope()) {
			type = byScopedName.get(typeName);
		}
		else {
			type = byName.get(typeName.getName());
		}
		
		return type;
	}
}
