package com.neaterbits.compiler.common.loader;

import com.neaterbits.compiler.common.ast.type.FullTypeName;

public interface ResolvedTypeDependency extends TypeInfo, TypeDependency {

	default FullTypeName geFullTypeName() {
		return getResolvedType().getFullTypeName();
	}
	
	ResolvedType getResolvedType();
}
