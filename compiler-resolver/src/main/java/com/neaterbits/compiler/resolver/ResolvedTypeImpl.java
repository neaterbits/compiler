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

final class ResolvedTypeImpl<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> extends BaseResolverType implements ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> {

	private final COMPLEXTYPE type;
	private final TypeName typeName;
	
	private final List<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> nestedTypes;
	private final List<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> extendsFrom;
	private final List<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> dependencies;

	public ResolvedTypeImpl(
			FileSpec file,
			TypeName typeName,
			TypeVariant typeVariant,
			COMPLEXTYPE type,
			List<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> nestedTypes,
			List<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> extendsFrom,
			List<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> dependencies) {
		
		super(file, new TypeSpec(typeName.toScopedName(), typeVariant));
		
		Objects.requireNonNull(file);
		Objects.requireNonNull(type);
		Objects.requireNonNull(typeName);
		
		if (extendsFrom.contains(this)) {
			throw new IllegalArgumentException();
		}

		if (extendsFrom.stream().anyMatch(e -> e.getCompleteName().equals(typeName))) {
			throw new IllegalArgumentException();
		}

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
	public Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> getNestedTypes() {
		return nestedTypes;
	}

	@Override
	public Collection<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> getExtendsFrom() {
		return extendsFrom;
	}

	@Override
	public Collection<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> getDependencies() {
		return dependencies;
	}
}
