package com.neaterbits.compiler.resolver.loader;

import java.util.function.BiConsumer;

import com.neaterbits.compiler.resolver.ReferenceType;
import com.neaterbits.compiler.resolver.loader.ast.TypeResolveMode;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.ast.typereference.TypeReference;

public interface CompiledTypeDependency {

	ScopedName getScopedName();
	
	ReferenceType getReferenceType();
	
	TypeReference getElement();

	BiConsumer<BaseType, TypeResolveMode> getUpdateOnResolve();
}
