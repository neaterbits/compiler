
package com.neaterbits.compiler.resolver.ast;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.resolver.types.BaseResolverType;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.resolver.types.TypeSpec;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.UserDefinedType;

final class ParsedType extends BaseResolverType implements CompiledType<UserDefinedType> {

	private final UserDefinedType type;
	
	private final List<CompiledType<UserDefinedType>> nestedTypes;
	private final List<CompiledTypeDependency> extendsFrom;
	private final List<CompiledTypeDependency> dependencies;

	ParsedType(
			FileSpec file,
			TypeSpec typeSpec,
			UserDefinedType type,
			List<CompiledType<UserDefinedType>> nestedTypes,
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
	public UserDefinedType getType() {
		return type;
	}
	
	@Override
	public TypeName getTypeName() {
		return type.getTypeName();
	}

	@Override
	public Collection<CompiledType<UserDefinedType>> getNestedTypes() {
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
