package com.neaterbits.compiler.resolver;

import java.util.Collection;

import com.neaterbits.compiler.codemap.TypeInfo;
import com.neaterbits.compiler.codemap.compiler.CrossReferenceUpdater;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.FieldInfo;
import com.neaterbits.compiler.util.model.MethodInfo;

public interface ResolvedTypeCodeMap<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> extends CrossReferenceUpdater {

	ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> getClassThisExtendsFrom(TypeName classType);

	TypeInfo getClassExtendsFromTypeInfo(TypeName classType);

	Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> getInterfacesImplement(TypeName classType);
	
	Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> getInterfacesExtendFrom(TypeName interfaceType);
	
	Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> getDirectExtendingThis(TypeName type);
	
	Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> getAllSubtypes(TypeName type);
	
	COMPLEXTYPE getType(int typeNo);
	
	TypeInfo getTypeInfo(TypeName type);

	FieldInfo getFieldInfo(TypeName type, String fieldName);

	MethodInfo getMethodInfo(TypeName type, String methodName, TypeName [] parameterTypes);
}
