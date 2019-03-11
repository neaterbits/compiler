package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.function.Consumer;

import com.neaterbits.compiler.resolver.types.ResolvedType;

class ResolveUtil {

	static <BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> void forEachResolvedTypeNested(
			Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> types,
			Consumer<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> forEachType) {
		
		for (ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> type : types) {

			forEachType.accept(type);
			
			if (type.getNestedTypes() != null) {
				forEachResolvedTypeNested(type.getNestedTypes(), forEachType);
			}
		}
	}
}
