package com.neaterbits.compiler.common.loader;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.resolver.ReferenceType;

public interface ResolvedTypeDependency {

	CompleteName getCompleteName();
	
	ReferenceType getReferenceType();
	
	TypeReference getElement();
}
