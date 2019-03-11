package com.neaterbits.compiler.resolver.types;

import java.util.Collection;

public interface CompiledType<COMPLEXTYPE> extends ResolveTypeInfo {

	TypeSpec getSpec();
	
	FileSpec getFile();
	
	COMPLEXTYPE getType();
	
	Collection<CompiledType<COMPLEXTYPE>> getNestedTypes();

	Collection<CompiledTypeDependency> getExtendsFrom();
	
	Collection<CompiledTypeDependency> getDependencies();
	
}
