package com.neaterbits.compiler.resolver;

import java.util.Collection;

import com.neaterbits.compiler.ast.block.MethodName;
import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.ast.type.CompleteName;
import com.neaterbits.compiler.ast.type.NamedType;
import com.neaterbits.compiler.ast.type.complex.ClassType;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.codemap.MethodInfo;
import com.neaterbits.compiler.codemap.TypeInfo;
import com.neaterbits.compiler.resolver.loader.ResolvedType;

public interface ResolvedTypeCodeMap {

	ResolvedType getClassThisExtendsFrom(CompleteName classType);

	TypeInfo getClassExtendsFromTypeInfo(CompleteName classType);

	Collection<ResolvedType> getInterfacesImplement(CompleteName classType);
	
	Collection<ResolvedType> getInterfacesExtendFrom(CompleteName interfaceType);
	
	Collection<ResolvedType> getDirectSubtypes(CompleteName type);
	
	Collection<ResolvedType> getAllSubtypes(CompleteName type);
	
	TypeInfo getTypeInfo(BaseType type);
	
	ComplexType<?, ?, ?> getType(int typeNo);
	
	default int getMethodNo(ClassType classType, MethodName methodName, NamedType [] parameterTypes) {
		
		final MethodInfo methodInfo = getMethodInfo(classType, methodName, parameterTypes);
		
		return methodInfo != null ? methodInfo.getMethodNo() : -1;
	}
	
	MethodInfo getMethodInfo(ClassType classType, MethodName methodName, NamedType [] parameterTypes);
	
}
