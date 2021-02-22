package com.neaterbits.compiler.common.loader;

import java.util.Collection;

import com.neaterbits.compiler.common.ast.ScopedName;

public interface CompiledType extends TypeInfo {

	TypeSpec getSpec();
	
	FileSpec getFile();
	
	Collection<CompiledType> getNestedTypes();

	Collection<ScopedName> getExtendsFrom();
	
	Collection<CompiledTypeDependency> getDependencies();

}
