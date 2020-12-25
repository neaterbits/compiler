package com.neaterbits.compiler.model.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neaterbits.build.types.ScopedName;
import com.neaterbits.build.types.TypesMap;
import com.neaterbits.compiler.model.common.ImportsModel;

public final class ScopedNameResolver {

    @Deprecated
	public static <T, COMPILATION_UNIT> T resolveScopedName(
			ScopedName toResolve,
			ReferenceLocation referenceType,
			COMPILATION_UNIT compilationUnit,
			ImportsModel<COMPILATION_UNIT> importsModel,
			ScopedName referencedFrom,
			TypesMap<T> compiledTypesMap) {

		T result = resolveScopedName(toResolve, compilationUnit, importsModel, referencedFrom, compiledTypesMap);

		if (result == null && referenceType == ReferenceLocation.STATIC_OR_STATIC_INSTANCE_METHOD_CALL) {

			if (toResolve.hasScope() && toResolve.getScope().size() > 1) {

				final ScopedName updatedScopeName = new ScopedName(
						toResolve.getScope().subList(0, toResolve.getScope().size() - 1),
						toResolve.getName());

				result = resolveScopedName(updatedScopeName, compilationUnit, importsModel, referencedFrom, compiledTypesMap);
			}
			// Class name as part of scopedName
			else if (toResolve.hasScope() && toResolve.getScope().size() == 1) {

				final ScopedName updatedScopeName = new ScopedName(null, toResolve.getScope().get(0));

				result = resolveScopedName(updatedScopeName, compilationUnit, importsModel, referencedFrom, compiledTypesMap);
			}
		}

		return result;
	}

	private static <T, COMPILATION_UNIT> T resolveScopedName(
			ScopedName scopedName,
			COMPILATION_UNIT compilationUnit,
			ImportsModel<COMPILATION_UNIT> importsModel,
			ScopedName referencedFrom,
			TypesMap<T> compiledTypesMap) {

	    return resolveScopedName(scopedName, compilationUnit, importsModel, referencedFrom.getScope(), compiledTypesMap);
	}

    public static <T, COMPILATION_UNIT> T resolveScopedName(
            ScopedName scopedName,
            COMPILATION_UNIT compilationUnit,
            ImportsModel<COMPILATION_UNIT> importsModel,
            List<String> referencedFrom,
            TypesMap<T> compiledTypesMap) {

		final T result;
		final T inSameNamespace;

		if (scopedName.hasScope()) {
			result = compiledTypesMap.lookupByScopedName(scopedName);
		}
		// Check same namespace as reference from
		else if (  referencedFrom != null && !referencedFrom.isEmpty()
		        && (null != (inSameNamespace = compiledTypesMap.lookupByScopedName(new ScopedName(referencedFrom, scopedName.getName()))))) {
			result = inSameNamespace;
		}
		// Try imports
		else {

			// Only type name, must look at imports

			final List<ScopedName> names = FileImports.getAllNameCombinations(compilationUnit, scopedName, importsModel);

 // System.out.println("## name combinations: " + names + " for " + scopedName);

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
