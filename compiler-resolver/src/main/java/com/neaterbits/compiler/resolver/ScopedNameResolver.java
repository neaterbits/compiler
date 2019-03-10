package com.neaterbits.compiler.resolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neaterbits.compiler.resolver.loader.IFileImports;
import com.neaterbits.compiler.util.ScopedName;

public final class ScopedNameResolver {
	
	public static <T> T resolveScopedName(ScopedName scopedName, ReferenceType referenceType, IFileImports fileImports, ScopedName referencedFrom, TypesMap<T> compiledTypesMap) {
		
		T result = resolveScopedName(scopedName, fileImports, referencedFrom, compiledTypesMap);
		
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

	private static <T> T resolveScopedName(ScopedName scopedName, IFileImports fileImports, ScopedName referencedFrom, TypesMap<T> compiledTypesMap) {
		
		final T result;
		final T inSameNamespace;
		
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
			
			final Map<ScopedName, T> matches = new HashMap<>();
			
			if (names != null) {
				for (ScopedName name : names) {
					final T type = compiledTypesMap.lookupByScopedName(name);
					
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
