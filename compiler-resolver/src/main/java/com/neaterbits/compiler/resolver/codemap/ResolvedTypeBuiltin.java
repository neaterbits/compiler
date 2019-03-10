package com.neaterbits.compiler.resolver.codemap;

import java.util.Collection;
import java.util.Objects;

import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.ast.NamespaceReference;
import com.neaterbits.compiler.ast.type.CompleteName;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.resolver.types.FileSpec;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.resolver.types.TypeSpec;

public final class ResolvedTypeBuiltin implements ResolvedType {

	private final BuiltinType builtinType;
	
	public ResolvedTypeBuiltin(BuiltinType builtinType) {
		
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
