package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.function.Consumer;

import com.neaterbits.compiler.resolver.types.ResolvedType;

class ResolveUtil {

	static <BUILTINTYPE, COMPLEXTYPE> void forEachResolvedTypeNested(
			Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> types,
			Consumer<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> forEachType) {
		
		for (ResolvedType<BUILTINTYPE, COMPLEXTYPE> type : types) {

			forEachType.accept(type);
			
			if (type.getNestedTypes() != null) {
				forEachResolvedTypeNested(type.getNestedTypes(), forEachType);
			}
		}
	}
}
