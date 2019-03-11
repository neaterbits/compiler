package com.neaterbits.compiler.resolver;

import java.util.Collection;

import com.neaterbits.compiler.codemap.MethodInfo;
import com.neaterbits.compiler.codemap.TypeInfo;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.util.TypeName;

public interface ResolvedTypeCodeMap<BUILTINTYPE, COMPLEXTYPE> {

	ResolvedType<BUILTINTYPE, COMPLEXTYPE> getClassThisExtendsFrom(TypeName classType);

	TypeInfo getClassExtendsFromTypeInfo(TypeName classType);

	Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> getInterfacesImplement(TypeName classType);
	
	Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> getInterfacesExtendFrom(TypeName interfaceType);
	
	Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> getDirectSubtypes(TypeName type);
	
	Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> getAllSubtypes(TypeName type);
	
	COMPLEXTYPE getType(int typeNo);
	
	TypeInfo getTypeInfo(TypeName type);

	MethodInfo getMethodInfo(TypeName type, String methodName, TypeName [] parameterTypes);
}
