
package com.neaterbits.compiler.resolver.types;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.model.common.UserDefinedTypeRef;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.TypeName;

public final class CompiledType extends BaseResolverType implements ResolveTypeInfo {

	private final UserDefinedTypeRef type;
	
	private final List<CompiledType> nestedTypes;
	private final List<CompiledTypeDependency> extendsFrom;
	private final List<CompiledTypeDependency> dependencies;

	public CompiledType(
			FileSpec file,
			TypeSpec typeSpec,
			UserDefinedTypeRef type,
			List<CompiledType> nestedTypes,
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

	public UserDefinedTypeRef getType() {
		return type;
	}
	
	@Override
	public TypeName getTypeName() {
		return type.getTypeName();
	}

	public Collection<CompiledType> getNestedTypes() {
		return nestedTypes;
	}

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
