package com.neaterbits.compiler.resolver.types;

import java.util.Collection;

import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.model.BuiltinTypeRef;
import com.neaterbits.compiler.util.model.UserDefinedTypeRef;

public interface ResolvedType extends ResolveTypeInfo {

	TypeSpec getSpec();
	
	FileSpec getFile();
	
	UserDefinedTypeRef getType();
	
	BuiltinTypeRef getBuiltinType();
	
	Collection<ResolvedType> getNestedTypes();

	Collection<ResolvedTypeDependency> getExtendsFrom();
	
	Collection<ResolvedTypeDependency> getDependencies();

}
