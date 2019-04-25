package com.neaterbits.compiler.resolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.util.ScopedName;

final class FileCachedResolvedTypes<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> {

	private final Map<ScopedName, ResolvedType> cached;
	
	FileCachedResolvedTypes() {
		this.cached = new HashMap<>();
	}
	
	ResolvedType get(ScopedName scopedName) {
		Objects.requireNonNull(scopedName);
		
		return cached.get(scopedName);
	}
	
	void put(ScopedName scopedName, ResolvedType resolvedType) {
		Objects.requireNonNull(scopedName);
		Objects.requireNonNull(resolvedType);

		final ResolvedType stored = cached.put(scopedName, resolvedType);
		
		if (stored != null) {
			throw new IllegalStateException("Already stored cached value for " + scopedName);
		}
	}
}
