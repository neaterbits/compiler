package com.neaterbits.compiler.common.resolver;

import java.util.Collection;

import com.neaterbits.compiler.common.ast.block.MethodName;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.type.NamedType;
import com.neaterbits.compiler.common.ast.type.complex.ClassType;
import com.neaterbits.compiler.common.loader.ResolvedType;
import com.neaterbits.compiler.common.resolver.codemap.MethodInfo;
import com.neaterbits.compiler.common.resolver.codemap.TypeInfo;

public interface CodeMap {

	ResolvedType getClassExtendsFrom(CompleteName classType);
	
	Collection<ResolvedType> getInterfacesImplement(CompleteName classType);
	
	Collection<ResolvedType> getInterfacesExtendFrom(CompleteName interfaceType);
	
	Collection<ResolvedType> getDirectSubtypes(CompleteName type);
	
	Collection<ResolvedType> getAllSubtypes(CompleteName type);
	
	TypeInfo getTypeInfo(BaseType type);
	
	default int getMethodNo(ClassType classType, MethodName methodName, NamedType [] parameterTypes) {
		
		final MethodInfo methodInfo = getMethodInfo(classType, methodName, parameterTypes);
		
		return methodInfo != null ? methodInfo.getMethodNo() : -1;
	}
	
	MethodInfo getMethodInfo(ClassType classType, MethodName methodName, NamedType [] parameterTypes);
	
}
