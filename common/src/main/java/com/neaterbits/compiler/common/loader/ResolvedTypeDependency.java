package com.neaterbits.compiler.common.loader;

import com.neaterbits.compiler.common.TypeReference;

public interface ResolvedTypeDependency extends TypeInfo, TypeDependency {

	TypeReference getElement();
	
	ResolvedType getResolvedType();
}
