package com.neaterbits.compiler.common.resolver;

import java.util.Collection;

import com.neaterbits.compiler.common.ast.block.MethodName;
import com.neaterbits.compiler.common.ast.type.FullTypeName;
import com.neaterbits.compiler.common.ast.type.NamedType;
import com.neaterbits.compiler.common.ast.type.complex.ClassType;
import com.neaterbits.compiler.common.loader.ResolvedType;
import com.neaterbits.compiler.common.resolver.codemap.MethodInfo;

public interface CodeMap {

	
	ResolvedType getClassExtendsFrom(FullTypeName classType);
	
	Collection<ResolvedType> getInterfacesImplement(FullTypeName classType);
	
	Collection<ResolvedType> getInterfacesExtendFrom(FullTypeName interfaceType);
	
	Collection<ResolvedType> getDirectSubtypes(FullTypeName type);
	
	Collection<ResolvedType> getAllSubtypes(FullTypeName type);
	
	default int getMethodNo(ClassType classType, MethodName methodName, NamedType [] parameterTypes) {
		
		final MethodInfo methodInfo = getMethodInfo(classType, methodName, parameterTypes);
		
		return methodInfo != null ? methodInfo.getMethodNo() : -1;
	}
	
	MethodInfo getMethodInfo(ClassType classType, MethodName methodName, NamedType [] parameterTypes);
	
}
