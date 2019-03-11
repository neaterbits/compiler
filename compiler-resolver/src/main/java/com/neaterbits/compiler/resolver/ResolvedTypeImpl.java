package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.resolver.types.BaseResolverType;
import com.neaterbits.compiler.resolver.types.FileSpec;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.resolver.types.TypeSpec;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.codemap.TypeVariant;

final class ResolvedTypeImpl<BUILTINTYPE, COMPLEXTYPE> extends BaseResolverType implements ResolvedType<BUILTINTYPE, COMPLEXTYPE> {

	private final COMPLEXTYPE type;
	private final TypeName typeName;
	
	private final List<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> nestedTypes;
	private final List<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE>> extendsFrom;
	private final List<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE>> dependencies;

	public ResolvedTypeImpl(
			FileSpec file,
			TypeName typeName,
			TypeVariant typeVariant,
			COMPLEXTYPE type,
			List<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> nestedTypes,
			List<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE>> extendsFrom,
			List<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE>> dependencies) {
		
		super(file, new TypeSpec(typeName.toScopedName(), typeVariant));
		
		Objects.requireNonNull(file);
		Objects.requireNonNull(type);
		Objects.requireNonNull(typeName);
		
		this.type = type;
		this.typeName = typeName;
		
		this.nestedTypes = nestedTypes != null ? Collections.unmodifiableList(nestedTypes) : null;
		this.extendsFrom = extendsFrom != null ? Collections.unmodifiableList(extendsFrom) : null;
		this.dependencies = dependencies != null ? Collections.unmodifiableList(dependencies) : null;
	}

	@Override
	public COMPLEXTYPE getType() {
		return type;
	}
	
	@Override
	public TypeName getTypeName() {
		return typeName;
	}

	@Override
	public BUILTINTYPE getBuiltinType() {
		return null;
	}

	@Override
	public Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> getNestedTypes() {
		return nestedTypes;
	}

	@Override
	public Collection<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE>> getExtendsFrom() {
		return extendsFrom;
	}

	@Override
	public Collection<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE>> getDependencies() {
		return dependencies;
	}
}
