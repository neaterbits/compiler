package com.neaterbits.compiler.common.loader;

import java.util.Collection;

import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.type.complex.ComplexType;

public interface ResolvedType extends TypeInfo {

	TypeSpec getSpec();
	
	FileSpec getFile();
	
	NamespaceReference getNamespace();
	
	ComplexType getType();
	
	Collection<ResolvedType> getNestedTypes();

	Collection<ResolvedTypeDependency> getExtendsFrom();
	
	Collection<ResolvedTypeDependency> getDependencies();

}
