package com.neaterbits.compiler.common.resolver;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.loader.BaseTypeInfo;
import com.neaterbits.compiler.common.loader.CompiledTypeDependency;
import com.neaterbits.compiler.common.loader.TypeVariant;

public class TestCompiledTypeDependency extends BaseTypeInfo implements CompiledTypeDependency {
	private final ReferenceType referenceType;
	
	public TestCompiledTypeDependency(ScopedName scopedName, TypeVariant typeVariant, ReferenceType referenceType) {
		super(scopedName, typeVariant);

		this.referenceType = referenceType;
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
