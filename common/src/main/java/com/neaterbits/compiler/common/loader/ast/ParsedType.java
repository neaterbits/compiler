package com.neaterbits.compiler.common.loader.ast;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.type.complex.ComplexType;
import com.neaterbits.compiler.common.loader.CompiledType;
import com.neaterbits.compiler.common.loader.CompiledTypeDependency;
import com.neaterbits.compiler.common.loader.FileSpec;
import com.neaterbits.compiler.common.loader.TypeSpec;

final class ParsedType extends BaseLoaderType implements CompiledType {

	private final ComplexType type;
	
	private final NamespaceReference namespace;
	private final List<CompiledType> nestedTypes;
	private final List<CompiledTypeDependency> extendsFrom;
	private final List<CompiledTypeDependency> dependencies;

	ParsedType(
			FileSpec file,
			TypeSpec typeSpec,
			NamespaceReference namespace,
			ComplexType type,
			List<CompiledType> nestedTypes,
			List<CompiledTypeDependency> extendsFrom,
			List<CompiledTypeDependency> dependencies) {

		super(file, typeSpec);
		
		Objects.requireNonNull(file);
		Objects.requireNonNull(namespace);
		Objects.requireNonNull(type);
		
		this.namespace = namespace;
		this.type = type;
		
		this.nestedTypes = nestedTypes;
		this.extendsFrom = extendsFrom;
		this.dependencies = dependencies;
	}

	
	@Override
	public NamespaceReference getNamespace() {
		return namespace;
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

	public Collection<CompiledTypeDependency> getDependencies() {
		return dependencies;
	}


	@Override
	public String toString() {
		return "ParsedType [ " + super.toString() + " nestedTypes=" + nestedTypes + ", extendsFrom=" + extendsFrom + ", dependencies="
				+ dependencies + "]";
	}

	
}
