package com.neaterbits.compiler.common.resolver.references;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.type.FullTypeName;
import com.neaterbits.compiler.common.loader.ResolvedTypeDependency;
import com.neaterbits.compiler.common.resolver.ReferenceType;

public final class TestResolvedTypeDependency implements ResolvedTypeDependency {

	private final FullTypeName fullTypeName;
	private final ReferenceType referenceType;

	public TestResolvedTypeDependency(FullTypeName fullTypeName, ReferenceType referenceType) {
		this.fullTypeName = fullTypeName;
		this.referenceType = referenceType;
	}
	@Override
	public FullTypeName getFullTypeName() {
		return fullTypeName;
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
