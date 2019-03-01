package com.neaterbits.compiler.common.resolver.codemap;

import java.util.Collection;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.type.complex.ComplexType;
import com.neaterbits.compiler.common.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.common.loader.FileSpec;
import com.neaterbits.compiler.common.loader.ResolvedType;
import com.neaterbits.compiler.common.loader.ResolvedTypeDependency;
import com.neaterbits.compiler.common.loader.TypeSpec;
import com.neaterbits.compiler.common.loader.TypeVariant;

final class ResolvedTypeBuiltin implements ResolvedType {

	private final BuiltinType builtinType;
	
	ResolvedTypeBuiltin(BuiltinType builtinType) {
		
		Objects.requireNonNull(builtinType);

		this.builtinType = builtinType;
	}

	@Override
	public TypeVariant getTypeVariant() {
		return TypeVariant.BUILTIN;
	}
	
	@Override
	public ScopedName getScopedName() {
		return builtinType.getCompleteName().toScopedName();
	}
	
	@Override
	public NamespaceReference getNamespace() {
		return builtinType.getNamespace();
	}

	@Override
	public CompleteName getCompleteName() {
		return builtinType.getCompleteName();
	}

	@Override
	public ComplexType<?, ?, ?> getType() {
		return null;
	}
	
	@Override
	public TypeSpec getSpec() {
		return null;
	}
	
	@Override
	public Collection<ResolvedType> getNestedTypes() {
		return null;
	}
	
	@Override
	public FileSpec getFile() {
		return null;
	}
	
	@Override
	public Collection<ResolvedTypeDependency> getExtendsFrom() {
		return null;
	}
	
	@Override
	public Collection<ResolvedTypeDependency> getDependencies() {
		return null;
	}
}
