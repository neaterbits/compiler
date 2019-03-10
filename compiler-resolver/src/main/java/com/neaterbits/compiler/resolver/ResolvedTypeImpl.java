package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.resolver.loader.FileSpec;
import com.neaterbits.compiler.resolver.loader.ResolvedType;
import com.neaterbits.compiler.resolver.loader.ResolvedTypeDependency;
import com.neaterbits.compiler.resolver.loader.TypeSpec;
import com.neaterbits.compiler.resolver.loader.ast.BaseLoaderType;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.codemap.TypeVariant;

final class ResolvedTypeImpl extends BaseLoaderType implements ResolvedType {

	private final ComplexType<?, ?, ?> type;
	
	private final List<ResolvedType> nestedTypes;
	private final List<ResolvedTypeDependency> extendsFrom;
	private final List<ResolvedTypeDependency> dependencies;

	public ResolvedTypeImpl(
			FileSpec file,
			ScopedName scopedName,
			TypeVariant typeVariant,
			ComplexType<?, ?, ?> type,
			List<ResolvedType> nestedTypes,
			List<ResolvedTypeDependency> extendsFrom,
			List<ResolvedTypeDependency> dependencies) {
		
		super(file, new TypeSpec(scopedName, typeVariant));
		
		Objects.requireNonNull(file);
		Objects.requireNonNull(type);
		
		this.type = type;
		
		this.nestedTypes = nestedTypes != null ? Collections.unmodifiableList(nestedTypes) : null;
		this.extendsFrom = extendsFrom != null ? Collections.unmodifiableList(extendsFrom) : null;
		this.dependencies = dependencies != null ? Collections.unmodifiableList(dependencies) : null;
	}

	@Override
	public ComplexType<?, ?, ?> getType() {
		return type;
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
