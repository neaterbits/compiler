
package com.neaterbits.compiler.resolver.ast;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.resolver.types.BaseResolverType;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.resolver.types.FileSpec;
import com.neaterbits.compiler.resolver.types.TypeSpec;
import com.neaterbits.compiler.util.TypeName;

final class ParsedType extends BaseResolverType implements CompiledType<ComplexType<?, ?, ?>> {

	private final ComplexType<?, ?, ?> type;
	
	private final List<CompiledType<ComplexType<?, ?, ?>>> nestedTypes;
	private final List<CompiledTypeDependency> extendsFrom;
	private final List<CompiledTypeDependency> dependencies;

	ParsedType(
			FileSpec file,
			TypeSpec typeSpec,
			ComplexType<?, ?, ?> type,
			List<CompiledType<ComplexType<?, ?, ?>>> nestedTypes,
			List<CompiledTypeDependency> extendsFrom,
			List<CompiledTypeDependency> dependencies) {

		super(file, typeSpec);
		
		Objects.requireNonNull(file);
		Objects.requireNonNull(type);
		
		this.type = type;
		
		this.nestedTypes = nestedTypes;
		this.extendsFrom = extendsFrom;
		this.dependencies = dependencies;
	}

	@Override
	public ComplexType<?, ?, ?> getType() {
		return type;
	}
	
	@Override
	public TypeName getTypeName() {
		return type.getCompleteName().toTypeName();
	}

	@Override
	public Collection<CompiledType<ComplexType<?, ?, ?>>> getNestedTypes() {
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
