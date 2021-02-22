package com.neaterbits.compiler.common.loader;

import java.util.Collection;

public interface ResolvedType extends TypeInfo {

	TypeSpec getSpec();
	
	FileSpec getFile();
	
	Collection<ResolvedType> getNestedTypes();

	Collection<ResolvedTypeDependency> getExtendsFrom();
	
	Collection<ResolvedTypeDependency> getDependencies();

}
