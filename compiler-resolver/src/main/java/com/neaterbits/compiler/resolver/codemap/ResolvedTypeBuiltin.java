package com.neaterbits.compiler.resolver.codemap;

import java.util.Collection;
import java.util.Objects;

import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.model.common.BuiltinTypeRef;
import com.neaterbits.compiler.model.common.UserDefinedTypeRef;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.resolver.types.TypeSpec;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;

public final class ResolvedTypeBuiltin
		implements ResolvedType {

	private final BuiltinTypeRef builtinType;
	
	public ResolvedTypeBuiltin(BuiltinTypeRef builtinType) {
		
		Objects.requireNonNull(builtinType);
		
		this.builtinType = builtinType;
	}

	@Override
	public ScopedName getScopedName() {
		return builtinType.getTypeName().toScopedName();
	}
	
	@Override
	public TypeName getTypeName() {
		return builtinType.getTypeName();
	}

	@Override
	public TypeVariant getTypeVariant() {
		return TypeVariant.BUILTIN;
	}

	@Override
	public UserDefinedTypeRef getType() {
		return null;
	}
	
	@Override
	public BuiltinTypeRef getBuiltinType() {
		return builtinType;
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
