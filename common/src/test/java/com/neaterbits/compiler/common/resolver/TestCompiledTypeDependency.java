package com.neaterbits.compiler.common.resolver;

import java.util.function.BiConsumer;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.loader.BaseTypeInfo;
import com.neaterbits.compiler.common.loader.CompiledTypeDependency;
import com.neaterbits.compiler.common.loader.TypeVariant;
import com.neaterbits.compiler.common.loader.ast.TypeResolveMode;

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

	@Override
	public BiConsumer<BaseType, TypeResolveMode> getUpdateOnResolve() {
		return null;
	}
}
