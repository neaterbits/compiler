package com.neaterbits.compiler.resolver.types;

import java.util.Collection;

public interface ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> extends ResolveTypeInfo {

	TypeSpec getSpec();
	
	FileSpec getFile();
	
	COMPLEXTYPE getType();
	
	BUILTINTYPE getBuiltinType();
	
	Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> getNestedTypes();

	Collection<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> getExtendsFrom();
	
	Collection<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> getDependencies();

}
