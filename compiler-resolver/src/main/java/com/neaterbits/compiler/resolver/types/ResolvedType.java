package com.neaterbits.compiler.resolver.types;

import java.util.Collection;

import com.neaterbits.compiler.util.FileSpec;

public interface ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> extends ResolveTypeInfo {

	TypeSpec getSpec();
	
	FileSpec getFile();
	
	COMPLEXTYPE getType();
	
	BUILTINTYPE getBuiltinType();
	
	Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> getNestedTypes();

	Collection<ResolvedTypeDependency> getExtendsFrom();
	
	Collection<ResolvedTypeDependency> getDependencies();

}
