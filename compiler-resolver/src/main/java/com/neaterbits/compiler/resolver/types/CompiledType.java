package com.neaterbits.compiler.resolver.types;

import java.util.Collection;

import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.model.UserDefinedTypeRef;

public interface CompiledType extends ResolveTypeInfo {

	TypeSpec getSpec();
	
	FileSpec getFile();
	
	UserDefinedTypeRef getType();
	
	Collection<CompiledType> getNestedTypes();

	Collection<CompiledTypeDependency> getExtendsFrom();
	
	Collection<CompiledTypeDependency> getDependencies();
	
}
