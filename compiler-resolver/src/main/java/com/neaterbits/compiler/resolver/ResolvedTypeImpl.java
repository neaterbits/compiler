package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.resolver.types.BaseResolverType;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.resolver.types.TypeSpec;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.BuiltinTypeRef;
import com.neaterbits.compiler.util.model.UserDefinedTypeRef;
import com.neaterbits.compiler.codemap.TypeVariant;

final class ResolvedTypeImpl extends BaseResolverType implements ResolvedType {

	private final UserDefinedTypeRef type;
	
	private final List<ResolvedType> nestedTypes;
	private final List<ResolvedTypeDependency> extendsFrom;
	private final List<ResolvedTypeDependency> dependencies;

	public ResolvedTypeImpl(
			FileSpec file,
			TypeVariant typeVariant,
			UserDefinedTypeRef type,
			List<ResolvedType> nestedTypes,
			List<ResolvedTypeDependency> extendsFrom,
			List<ResolvedTypeDependency> dependencies) {
		
		super(file, new TypeSpec(type.toScopedName(), typeVariant));
		
		Objects.requireNonNull(file);
		Objects.requireNonNull(type);
		
		if (extendsFrom != null) {
			if (extendsFrom.stream().anyMatch(ef -> ef.getCompleteName().equals(getTypeName()))) {
				throw new IllegalArgumentException();
			}
	
			if (extendsFrom.stream().anyMatch(e -> e.getCompleteName().equals(type.getTypeName()))) {
				throw new IllegalArgumentException();
			}
		}

		this.type = type;
		
		this.nestedTypes = nestedTypes != null ? Collections.unmodifiableList(nestedTypes) : null;
		this.extendsFrom = extendsFrom != null ? Collections.unmodifiableList(extendsFrom) : null;
		this.dependencies = dependencies != null ? Collections.unmodifiableList(dependencies) : null;
	}

	@Override
	public UserDefinedTypeRef getType() {
		return type;
	}
	
	@Override
	public TypeName getTypeName() {
		return type.getTypeName();
	}

	@Override
	public BuiltinTypeRef getBuiltinType() {
		return null;
	}

	@Override
	public Collection<ResolvedType> getNestedTypes() {
		return nestedTypes;
	}

	@Override
	public Collection<ResolvedTypeDependency> getExtendsFrom() {
		return extendsFrom;
	}

	@Override
	public Collection<ResolvedTypeDependency> getDependencies() {
		return dependencies;
	}
}
