package com.neaterbits.compiler.common.loader;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.resolver.ReferenceType;

public interface CompiledTypeDependency {

	ScopedName getScopedName();
	
	ReferenceType getReferenceType();
	
	TypeReference getElement();

}
