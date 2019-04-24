package com.neaterbits.compiler.resolver.codemap;

import java.util.Collection;
import java.util.Objects;

import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.resolver.types.TypeSpec;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;

public final class ResolvedTypeBuiltin<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
		implements ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> {

	private final BUILTINTYPE builtinType;
	private final TypeName typeName;
	
	public ResolvedTypeBuiltin(BUILTINTYPE builtinType, TypeName typeName) {
		
		Objects.requireNonNull(builtinType);
		Objects.requireNonNull(typeName);
		
		this.typeName = typeName;
		this.builtinType = builtinType;
	}

	@Override
	public ScopedName getScopedName() {
		return typeName.toScopedName();
	}
	
	@Override
	public TypeName getTypeName() {
		return typeName;
	}

	@Override
	public TypeVariant getTypeVariant() {
		return TypeVariant.BUILTIN;
	}

	@Override
	public COMPLEXTYPE getType() {
		return null;
	}
	
	@Override
	public BUILTINTYPE getBuiltinType() {
		return builtinType;
	}

	@Override
	public TypeSpec getSpec() {
		return null;
	}
	
	@Override
	public Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> getNestedTypes() {
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
