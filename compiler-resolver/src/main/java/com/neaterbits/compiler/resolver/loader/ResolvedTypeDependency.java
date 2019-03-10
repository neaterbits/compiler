package com.neaterbits.compiler.resolver.loader;

import com.neaterbits.compiler.ast.type.CompleteName;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.resolver.ReferenceType;

public interface ResolvedTypeDependency {

	CompleteName getCompleteName();
	
	ReferenceType getReferenceType();
	
	TypeReference getElement();
	
	TypeVariant getTypeVariant();
}
