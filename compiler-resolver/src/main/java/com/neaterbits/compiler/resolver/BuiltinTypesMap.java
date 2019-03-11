package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.util.ScopedName;

final class BuiltinTypesMap<BUILTINTYPE> {

	private final Map<ScopedName, BUILTINTYPE> byScopedName;
	private final Map<String, BUILTINTYPE> byName;
	
	BuiltinTypesMap(Collection<BUILTINTYPE> builtinTypes, ASTBuiltinTypeModel<BUILTINTYPE> astModel) {

		this.byScopedName = new HashMap<>(builtinTypes.size());
		this.byName = new HashMap<>(builtinTypes.size());

		for (BUILTINTYPE builtinType : builtinTypes) {
			byScopedName.put(astModel.getBuiltinTypeScopedName(builtinType), builtinType);
			
			byName.put(astModel.getBuiltinTypeNameString(builtinType), builtinType);
		}
	}


	BUILTINTYPE lookupType(ScopedName typeName) {
		
		Objects.requireNonNull(typeName);
		
		final BUILTINTYPE type;
		
		if (typeName.hasScope()) {
			type = byScopedName.get(typeName);
		}
		else {
			type = byName.get(typeName.getName());
		}
		
		return type;
	}
}