package com.neaterbits.compiler.resolver.types;

import java.util.Collection;

public interface ResolvedType<BUILTINTYPE, COMPLEXTYPE> extends ResolveTypeInfo {

	TypeSpec getSpec();
	
	FileSpec getFile();
	
	COMPLEXTYPE getType();
	
	BUILTINTYPE getBuiltinType();
	
	Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> getNestedTypes();

	Collection<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE>> getExtendsFrom();
	
	Collection<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE>> getDependencies();

}
