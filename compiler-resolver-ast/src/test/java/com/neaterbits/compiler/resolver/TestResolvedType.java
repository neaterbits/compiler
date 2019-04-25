package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.List;

import com.neaterbits.compiler.resolver.types.BaseResolverType;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.resolver.types.TypeSpec;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.UserDefinedType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.codemap.TypeVariant;

public class TestResolvedType extends BaseResolverType implements ResolvedType<BuiltinType, UserDefinedType, TypeName> {
	
	private final UserDefinedType type;
	
	private final List<ResolvedType<BuiltinType, UserDefinedType, TypeName>> nestedTypes;
	private final List<ResolvedTypeDependency> extendsFrom;
	private final List<ResolvedTypeDependency> dependencies;
	
	public TestResolvedType(
			FileSpec file,
			ScopedName scopedName,
			TypeVariant typeVariant,
			UserDefinedType type,
			List<ResolvedType<BuiltinType, UserDefinedType, TypeName>> nestedTypes,
			List<ResolvedTypeDependency> extendsFrom,
			List<ResolvedTypeDependency> dependencies) {

		super(file, new TypeSpec(scopedName, typeVariant));

		this.type = type;
		
		this.nestedTypes = nestedTypes;
		this.extendsFrom = extendsFrom;
		this.dependencies = dependencies;
	}

	public TestResolvedType(
			FileSpec file,
			ScopedName scopedName,
			TypeVariant typeVariant,
			UserDefinedType type) {
	
		this(file, scopedName, typeVariant, type, null, null, null);
	}

	@Override
	public UserDefinedType getType() {
		return type;
	}
	
	@Override
	public TypeName getTypeName() {
		return type != null ? type.getTypeName() : null;
	}

	@Override
	public BuiltinType getBuiltinType() {
		return null;
	}

	@Override
	public Collection<ResolvedType<BuiltinType, UserDefinedType, TypeName>> getNestedTypes() {
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
