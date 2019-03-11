package com.neaterbits.compiler.resolver.references;

import java.util.Objects;

import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.resolver.ReferenceType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.util.TypeName;

public final class TestResolvedTypeDependency implements ResolvedTypeDependency<BuiltinType, ComplexType<?, ?, ?>> {

	private final TypeName completeName;
	private final ReferenceType referenceType;
	private final TypeVariant typeVariant;

	public TestResolvedTypeDependency(TypeName completeName, ReferenceType referenceType, TypeVariant typeVariant) {

		Objects.requireNonNull(completeName);
		Objects.requireNonNull(referenceType);
		Objects.requireNonNull(typeVariant);
		
		this.completeName = completeName;
		this.referenceType = referenceType;
		this.typeVariant = typeVariant;
	}
	
	@Override
	public TypeName getCompleteName() {
		return completeName;
	}
	
	@Override
	public ReferenceType getReferenceType() {
		return referenceType;
	}

	@Override
	public TypeVariant getTypeVariant() {
		return typeVariant;
	}

	@Override
	public boolean shouldUpdateOnResolve() {
		return false;
	}

	@Override
	public void updateOnResolve(ComplexType<?, ?, ?> type) {
		
	}

	@Override
	public void replaceWithComplexType(ComplexType<?, ?, ?> complexType) {
		
	}

	@Override
	public void replaceWithBuiltinType(BuiltinType builtinType) {
		
	}
}
