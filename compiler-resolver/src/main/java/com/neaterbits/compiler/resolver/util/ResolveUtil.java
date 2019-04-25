package com.neaterbits.compiler.resolver.util;

import java.util.Collection;
import java.util.function.Consumer;

import com.neaterbits.compiler.resolver.types.ResolvedType;

public class ResolveUtil {

	public static <BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> void forEachResolvedTypeNested(
			Collection<ResolvedType> types,
			Consumer<ResolvedType> forEachType) {
		
		for (ResolvedType type : types) {

			forEachType.accept(type);
			
			if (type.getNestedTypes() != null) {
				forEachResolvedTypeNested(type.getNestedTypes(), forEachType);
			}
		}
	}
}
