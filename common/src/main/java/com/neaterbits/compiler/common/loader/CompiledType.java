package com.neaterbits.compiler.common.loader;

import java.util.Collection;

import com.neaterbits.compiler.common.ast.type.complex.ComplexType;

public interface CompiledType extends TypeInfo {

	TypeSpec getSpec();
	
	FileSpec getFile();
	
	ComplexType getType();
	
	Collection<CompiledType> getNestedTypes();

	Collection<CompiledTypeDependency> getExtendsFrom();
	
	Collection<CompiledTypeDependency> getDependencies();

}
