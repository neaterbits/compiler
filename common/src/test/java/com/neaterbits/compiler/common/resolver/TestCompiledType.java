package com.neaterbits.compiler.common.resolver;

import java.util.Collection;

import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.ast.type.complex.ComplexType;
import com.neaterbits.compiler.common.loader.CompiledType;
import com.neaterbits.compiler.common.loader.CompiledTypeDependency;
import com.neaterbits.compiler.common.loader.FileSpec;
import com.neaterbits.compiler.common.loader.TypeSpec;
import com.neaterbits.compiler.common.loader.TypeVariant;
import com.neaterbits.compiler.common.loader.ast.BaseLoaderType;

public class TestCompiledType extends BaseLoaderType implements CompiledType {

	private final ComplexType type;
	
	private final Collection<CompiledType> nestedTypes;
	private final Collection<CompiledTypeDependency> extendsFrom;
	private final Collection<CompiledTypeDependency> dependencies;
	
	
	public TestCompiledType(
			FileSpec file,
			TypeSpec typeSpec,
			ComplexType type,
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
			ComplexType type,
			Collection<CompiledType> nestedTypes,
			Collection<CompiledTypeDependency> extendsFrom,
			Collection<CompiledTypeDependency> dependencies) {
		this(file, new TypeSpec(scopedName, typeVariant), type, nestedTypes, extendsFrom, dependencies);
	}
	
	@Override
	public ComplexType getType() {
		return type;
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
