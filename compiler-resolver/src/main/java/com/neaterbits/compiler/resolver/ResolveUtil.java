package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.function.Consumer;

import com.neaterbits.compiler.resolver.types.ResolvedType;

class ResolveUtil {

	static void forEachResolvedTypeNested(Collection<ResolvedType> types, Consumer<ResolvedType> forEachType) {
		
		for (ResolvedType type : types) {

			forEachType.accept(type);
			
			if (type.getNestedTypes() != null) {
				forEachResolvedTypeNested(type.getNestedTypes(), forEachType);
			}
		}
	}
}
