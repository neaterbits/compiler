package com.neaterbits.compiler.resolver;

import java.util.Collection;

import com.neaterbits.compiler.resolver.types.BaseResolverType;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.resolver.types.TypeSpec;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.UserDefinedTypeRef;
import com.neaterbits.compiler.codemap.TypeVariant;

public class TestCompiledType extends BaseResolverType implements CompiledType {

	private final UserDefinedTypeRef type;
	
	private final Collection<CompiledType> nestedTypes;
	private final Collection<CompiledTypeDependency> extendsFrom;
	private final Collection<CompiledTypeDependency> dependencies;
	
	
	public TestCompiledType(
			FileSpec file,
			TypeSpec typeSpec,
			UserDefinedTypeRef type,
			Collection<CompiledType> nestedTypes,
			Collection<CompiledTypeDependency> extendsFrom,
			Collection<CompiledTypeDependency> dependencies) {
		super(file, typeSpec);

		this.type = type;
		
		this.nestedTypes = nestedTypes;
		this.extendsFrom = extendsFrom;
		this.dependencies = dependencies;
	}

	public TestCompiledType(
			FileSpec file,
			ScopedName scopedName,
			TypeVariant typeVariant,
			UserDefinedTypeRef type,
			Collection<CompiledType> nestedTypes,
			Collection<CompiledTypeDependency> extendsFrom,
			Collection<CompiledTypeDependency> dependencies) {
		this(file, new TypeSpec(scopedName, typeVariant), type, nestedTypes, extendsFrom, dependencies);
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
	public Collection<CompiledType> getNestedTypes() {
		return nestedTypes;
	}

	@Override
	public Collection<CompiledTypeDependency> getExtendsFrom() {
		return extendsFrom;
	}

	@Override
	public Collection<CompiledTypeDependency> getDependencies() {
		return dependencies;
	}
}
