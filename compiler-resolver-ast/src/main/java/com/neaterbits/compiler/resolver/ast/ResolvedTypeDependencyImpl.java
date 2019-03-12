package com.neaterbits.compiler.resolver.ast;

import java.util.Objects;
import java.util.function.BiConsumer;

import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.ast.typereference.BuiltinTypeReference;
import com.neaterbits.compiler.ast.typereference.ComplexTypeReference;
import com.neaterbits.compiler.ast.typereference.ResolveLaterTypeReference;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.resolver.ReferenceType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.TypeResolveMode;

final class ResolvedTypeDependencyImpl implements ResolvedTypeDependency<BuiltinType, ComplexType<?, ?, ?>, TypeName> {
	private final TypeName completeName;
	private final ReferenceType referenceType;
	private final TypeReference element;
	private final TypeResolveMode typeResolveMode;
	private final TypeVariant typeVariant;
	private final BiConsumer<BaseType, TypeResolveMode> updateOnResolve;
	
	ResolvedTypeDependencyImpl(
			TypeName completeName,
			ReferenceType referenceType,
			TypeReference element,
			TypeResolveMode typeResolveMode,
			TypeVariant typeVariant,
			BiConsumer<BaseType, TypeResolveMode> updateOnResolve) {

		Objects.requireNonNull(completeName);
		Objects.requireNonNull(referenceType);
		Objects.requireNonNull(element);
		Objects.requireNonNull(typeResolveMode);
//		Objects.requireNonNull(typeVariant);

		this.completeName = completeName;
		this.referenceType = referenceType;
		this.element = element;
		this.typeResolveMode = typeResolveMode;
		this.typeVariant = typeVariant;
		this.updateOnResolve = updateOnResolve;
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

	TypeResolveMode getTypeResolveMode() {
		return typeResolveMode;
	}

	BiConsumer<BaseType, TypeResolveMode> getUpdateOnResolve() {
		return updateOnResolve;
	}

	@Override
	public boolean shouldUpdateOnResolve() {
		return updateOnResolve != null;
	}

	@Override
	public void updateOnResolve(ComplexType<?, ?, ?> type) {
		updateOnResolve.accept(type, typeResolveMode);
	}

	@Override
	public void replaceWithComplexType(ComplexType<?, ?, ?> complexType) {
		
		if (!(element instanceof ResolveLaterTypeReference)) {
			throw new IllegalStateException();
		}
		
		element.replaceWith(new ComplexTypeReference(element.getContext(), complexType));
	}

	@Override
	public void replaceWithBuiltinType(BuiltinType builtinType) {

		if (!(element instanceof ResolveLaterTypeReference)) {
			throw new IllegalStateException();
		}

		element.replaceWith(new BuiltinTypeReference(element.getContext(), builtinType));
	}
}
