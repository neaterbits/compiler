package com.neaterbits.compiler.common.loader;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.type.FullTypeName;
import com.neaterbits.compiler.common.resolver.ReferenceType;

public interface ResolvedTypeDependency {

	FullTypeName getFullTypeName();
	
	ReferenceType getReferenceType();
	
	TypeReference getElement();
}
