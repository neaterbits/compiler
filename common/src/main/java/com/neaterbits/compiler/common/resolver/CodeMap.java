package com.neaterbits.compiler.common.resolver;

import java.util.Collection;

import com.neaterbits.compiler.common.ast.type.FullTypeName;
import com.neaterbits.compiler.common.loader.ResolvedType;

public interface CodeMap {
	
	ResolvedType getClassExtendsFrom(FullTypeName classType);
	
	Collection<ResolvedType> getInterfacesImplement(FullTypeName classType);
	
	Collection<ResolvedType> getInterfacesExtendFrom(FullTypeName interfaceType);
	
	Collection<ResolvedType> getDirectSubtypes(FullTypeName type);
	
	Collection<ResolvedType> getAllSubtypes(FullTypeName type);
	
}
