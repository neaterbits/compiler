package com.neaterbits.compiler.resolver.types;

import java.util.Collection;

import com.neaterbits.compiler.util.FileSpec;

public interface CompiledType<COMPLEXTYPE> extends ResolveTypeInfo {

	TypeSpec getSpec();
	
	FileSpec getFile();
	
	COMPLEXTYPE getType();
	
	Collection<CompiledType<COMPLEXTYPE>> getNestedTypes();

	Collection<CompiledTypeDependency> getExtendsFrom();
	
	Collection<CompiledTypeDependency> getDependencies();
	
}
