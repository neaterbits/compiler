package com.neaterbits.compiler.resolver;

import java.util.Collection;

import com.neaterbits.compiler.codemap.TypeInfo;
import com.neaterbits.compiler.codemap.compiler.CrossReferenceUpdater;
import com.neaterbits.compiler.model.common.UserDefinedTypeRef;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.types.FieldInfo;
import com.neaterbits.compiler.types.MethodInfo;
import com.neaterbits.compiler.util.TypeName;

public interface ResolvedTypeCodeMap extends CrossReferenceUpdater {

	ResolvedType getClassThisExtendsFrom(TypeName classType);

	TypeInfo getClassExtendsFromTypeInfo(TypeName classType);

	Collection<ResolvedType> getInterfacesImplement(TypeName classType);
	
	Collection<ResolvedType> getInterfacesExtendFrom(TypeName interfaceType);
	
	Collection<ResolvedType> getDirectExtendingThis(TypeName type);
	
	Collection<ResolvedType> getAllSubtypes(TypeName type);
	
	UserDefinedTypeRef getType(int typeNo);
	
	TypeInfo getTypeInfo(TypeName type);

	FieldInfo getFieldInfo(TypeName type, String fieldName);

	MethodInfo getMethodInfo(TypeName type, String methodName, TypeName [] parameterTypes);
}
