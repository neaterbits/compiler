package com.neaterbits.compiler.common.resolver;

import java.util.Collection;

import com.neaterbits.compiler.common.loader.ResolvedType;
import com.neaterbits.compiler.common.loader.TypeSpec;

public interface CodeMap {
	
	ResolvedType getClassExtendsFrom(TypeSpec classType);
	
	Collection<ResolvedType> getInterfacesImplement(TypeSpec classType);
	
	Collection<ResolvedType> getInterfacesExtendFrom(TypeSpec interfaceType);
	
	Collection<ResolvedType> getDirectSubtypes(TypeSpec type);
	
	Collection<ResolvedType> getAllSubtypes(TypeSpec type);
	
}
