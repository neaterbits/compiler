package com.neaterbits.compiler.resolver;

import java.util.function.BiConsumer;

import com.neaterbits.compiler.resolver.ReferenceType;
import com.neaterbits.compiler.resolver.ast.TypeResolveMode;
import com.neaterbits.compiler.resolver.types.BaseTypeInfo;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.codemap.TypeVariant;

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
