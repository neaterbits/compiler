package com.neaterbits.compiler.common.resolver;

import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.loader.BaseTypeInfo;
import com.neaterbits.compiler.common.loader.ResolvedType;
import com.neaterbits.compiler.common.loader.ResolvedTypeDependency;
import com.neaterbits.compiler.common.loader.TypeVariant;

public class TestDependency extends BaseTypeInfo implements ResolvedTypeDependency {

	private final ReferenceType referenceType;
	
	public TestDependency(ScopedName scopedName, TypeVariant typeVariant, ReferenceType referenceType) {
		super(scopedName, typeVariant);
		
		this.referenceType = referenceType;
	}
	
	public TestDependency(ResolvedType resolvedType, ReferenceType referenceType) {
		this(resolvedType.getScopedName(), resolvedType.getTypeVariant(), referenceType);
	}

	@Override
	public ReferenceType getReferenceType() {
		return referenceType;
	}
}
