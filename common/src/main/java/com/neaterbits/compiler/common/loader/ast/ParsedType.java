package com.neaterbits.compiler.common.loader.ast;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.loader.CompiledType;
import com.neaterbits.compiler.common.loader.CompiledTypeDependency;
import com.neaterbits.compiler.common.loader.FileSpec;
import com.neaterbits.compiler.common.loader.TypeSpec;

final class ParsedType extends BaseLoaderType implements CompiledType {

	private final List<CompiledType> nestedTypes;
	private final List<ScopedName> extendsFrom;
	private final List<CompiledTypeDependency> dependencies;

	ParsedType(
			FileSpec file,
			TypeSpec typeSpec,
			List<CompiledType> nestedTypes,
			List<ScopedName> extendsFrom,
			List<CompiledTypeDependency> dependencies) {

		super(file, typeSpec);
		
		Objects.requireNonNull(file);
		
		this.nestedTypes = nestedTypes;
		this.extendsFrom = extendsFrom;
		this.dependencies = dependencies;
	}


	@Override
	public Collection<CompiledType> getNestedTypes() {
		return nestedTypes;
	}

	@Override
	public Collection<ScopedName> getExtendsFrom() {
		return extendsFrom;
	}

	public Collection<CompiledTypeDependency> getDependencies() {
		return dependencies;
	}


	@Override
	public String toString() {
		return "ParsedType [ " + super.toString() + " nestedTypes=" + nestedTypes + ", extendsFrom=" + extendsFrom + ", dependencies="
				+ dependencies + "]";
	}

	
}
