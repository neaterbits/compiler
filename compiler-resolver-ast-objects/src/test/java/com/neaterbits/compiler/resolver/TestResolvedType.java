package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.resolver.types.BaseResolverType;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.resolver.types.TypeSpec;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.model.common.BuiltinTypeRef;
import com.neaterbits.compiler.model.common.UserDefinedTypeRef;

public class TestResolvedType extends BaseResolverType implements ResolvedType {
	
	private final UserDefinedTypeRef type;
	
	private final List<ResolvedType> nestedTypes;
	private final List<ResolvedTypeDependency> extendsFrom;
	private final List<ResolvedTypeDependency> dependencies;
	
	public TestResolvedType(
			FileSpec file,
			ScopedName scopedName,
			TypeVariant typeVariant,
			UserDefinedTypeRef type,
			List<ResolvedType> nestedTypes,
			List<ResolvedTypeDependency> extendsFrom,
			List<ResolvedTypeDependency> dependencies) {

		super(file, new TypeSpec(scopedName, typeVariant));

		Objects.requireNonNull(type);
		
		this.type = type;
		
		this.nestedTypes = nestedTypes;
		this.extendsFrom = extendsFrom;
		this.dependencies = dependencies;
	}

	public TestResolvedType(
			FileSpec file,
			ScopedName scopedName,
			TypeVariant typeVariant,
			UserDefinedTypeRef type) {
	
		this(file, scopedName, typeVariant, type, null, null, null);
	}

	@Override
	public UserDefinedTypeRef getType() {
		return type;
	}
	
	@Override
	public TypeName getTypeName() {
		return type != null ? type.getTypeName() : null;
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
