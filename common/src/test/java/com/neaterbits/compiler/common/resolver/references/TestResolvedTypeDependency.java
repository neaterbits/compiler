package com.neaterbits.compiler.common.resolver.references;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.loader.ResolvedTypeDependency;
import com.neaterbits.compiler.common.resolver.ReferenceType;

public final class TestResolvedTypeDependency implements ResolvedTypeDependency {

	private final CompleteName completeName;
	private final ReferenceType referenceType;

	public TestResolvedTypeDependency(CompleteName completeName, ReferenceType referenceType) {
		this.completeName = completeName;
		this.referenceType = referenceType;
	}
	@Override
	public CompleteName getCompleteName() {
		return completeName;
	}
	@Override
	public ReferenceType getReferenceType() {
		return referenceType;
	}
	@Override
	public TypeReference getElement() {
		return null;
	}
}
