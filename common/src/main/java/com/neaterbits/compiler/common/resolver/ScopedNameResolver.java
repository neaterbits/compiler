package com.neaterbits.compiler.common.resolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.loader.CompiledType;
import com.neaterbits.compiler.common.loader.FileImports;

final class ScopedNameResolver {
	static CompiledType resolveScopedName(ScopedName scopedName, ReferenceType referenceType, FileImports fileImports, ScopedName referencedFrom, CompiledTypesMap compiledTypesMap) {
		
		CompiledType result = resolveScopedName(scopedName, fileImports, referencedFrom, compiledTypesMap);
		
		if (result == null && referenceType == ReferenceType.STATIC_OR_STATIC_INSTANCE_METHOD_CALL) {

			if (scopedName.hasScope() && scopedName.getScope().size() > 1) {
			
				final ScopedName updatedScopeName = new ScopedName(scopedName.getScope().subList(0, scopedName.getScope().size() - 1), scopedName.getName());
				
				result = resolveScopedName(updatedScopeName, fileImports, referencedFrom, compiledTypesMap);
			}
			// Class name as part of scopedName
			else if (scopedName.hasScope() && scopedName.getScope().size() == 1) {

				final ScopedName updatedScopeName = new ScopedName(null, scopedName.getScope().get(0));
				
				result = resolveScopedName(updatedScopeName, fileImports, referencedFrom, compiledTypesMap);
			}
		}
		
		return result;
	}

	static CompiledType resolveScopedName(ScopedName scopedName, FileImports fileImports, ScopedName referencedFrom, CompiledTypesMap compiledTypesMap) {
		
		final CompiledType result;
		final CompiledType inSameNamespace;
		
		if (scopedName.hasScope()) {
			result = compiledTypesMap.lookupByScopedName(scopedName);
		}
		// Check same namespace as reference from
		else if (referencedFrom.hasScope() && (null != (inSameNamespace = compiledTypesMap.lookupByScopedName(new ScopedName(referencedFrom.getScope(), scopedName.getName()))))) {
			result = inSameNamespace;
		}
		// Try imports
		else {
			
			// Only type name, must look at imports
			
			final List<ScopedName> names = fileImports.getAllNameCombinations(scopedName);
			
System.out.println("## name combinations: " + names + " for " + scopedName);
			
			final Map<ScopedName, CompiledType> matches = new HashMap<>();
			
			if (names != null) {
				for (ScopedName name : names) {
					final CompiledType type = compiledTypesMap.lookupByScopedName(name);
					
					if (type != null) {
						// Make sure class-part matches
						if (name.getName().equals(scopedName.getName())) {
							matches.put(name, type);
						}
					}
				}
			}
			
			switch (matches.size()) {
			case 0:
				result = null;
				break;
	
			case 1:
				result = matches.values().iterator().next();
				break;
				
			default:
				throw new IllegalStateException("Multiple matches for " + scopedName);
			}
		}

		return result;
	}

}
