package com.neaterbits.compiler.common.loader;

import java.util.function.BiConsumer;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.loader.ast.TypeResolveMode;
import com.neaterbits.compiler.common.resolver.ReferenceType;

public interface CompiledTypeDependency {

	ScopedName getScopedName();
	
	ReferenceType getReferenceType();
	
	TypeReference getElement();

	BiConsumer<BaseType, TypeResolveMode> getUpdateOnResolve();
}
