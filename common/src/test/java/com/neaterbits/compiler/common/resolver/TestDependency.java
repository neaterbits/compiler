package com.neaterbits.compiler.common.resolver;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.loader.BaseTypeInfo;
import com.neaterbits.compiler.common.loader.ResolvedType;
import com.neaterbits.compiler.common.loader.ResolvedTypeDependency;
import com.neaterbits.compiler.common.loader.TypeVariant;

public class TestDependency extends BaseTypeInfo implements ResolvedTypeDependency {

	private final ReferenceType referenceType;
	private final TypeReference element;
	private final ResolvedType resolvedType;
	
	private TestDependency(ScopedName scopedName, TypeVariant typeVariant, ReferenceType referenceType, TypeReference element, ResolvedType resolvedType) {
		super(scopedName, typeVariant);
		
		this.referenceType = referenceType;
		this.element = element;
		this.resolvedType = resolvedType;
	}
	
	public TestDependency(ResolvedType resolvedType, ReferenceType referenceType, TypeReference element) {
		this(resolvedType.getScopedName(), resolvedType.getTypeVariant(), referenceType, element, resolvedType);
	}

	@Override
	public ReferenceType getReferenceType() {
		return referenceType;
	}

	@Override
	public TypeReference getElement() {
		return element;
	}

	@Override
	public ResolvedType getResolvedType() {
		return resolvedType;
	}
}
