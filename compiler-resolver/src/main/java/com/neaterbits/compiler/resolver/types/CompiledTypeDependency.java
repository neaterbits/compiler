package com.neaterbits.compiler.resolver.types;

import com.neaterbits.compiler.resolver.ReferenceType;
import com.neaterbits.compiler.util.ScopedName;

public interface CompiledTypeDependency {

	ScopedName getScopedName();
	
	ReferenceType getReferenceType();
	
}
