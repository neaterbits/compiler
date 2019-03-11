package com.neaterbits.compiler.resolver;


import com.neaterbits.compiler.resolver.ReferenceType;
import com.neaterbits.compiler.resolver.types.BaseTypeInfo;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.codemap.TypeVariant;

public class TestCompiledTypeDependency extends BaseTypeInfo implements CompiledTypeDependency {
	private final ReferenceType referenceType;
	
	public TestCompiledTypeDependency(ScopedName scopedName, TypeVariant typeVariant, ReferenceType referenceType) {
		super(scopedName, typeVariant);

		this.referenceType = referenceType;
	}

	@Override
	public TypeName getTypeName() {
		return null;
	}

	@Override
	public ReferenceType getReferenceType() {
		return referenceType;
	}
}
