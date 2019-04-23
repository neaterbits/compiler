package com.neaterbits.compiler.resolver.passes.namereferenceresolve;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.TypeName;

final class PrimaryListEntry {

	private final long primaryListReference;
	
	// type of last entry evaluated in primary list
	private final List<TypeName> types;

	public PrimaryListEntry(long primaryListReference) {
		this.primaryListReference = primaryListReference;
		this.types = new ArrayList<>();
	}

	long getPrimaryListReference() {
		return primaryListReference;
	}

	void addType(TypeName type) {
		Objects.requireNonNull(type);
		
		types.add(type);
	}

	List<TypeName> getTypes() {
		return types;
	}

	boolean isEmpty() {
		return types.isEmpty();
	}
}
