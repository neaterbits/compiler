package com.neaterbits.compiler.codemap.compiler;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.build.types.TypesMap;
import com.neaterbits.compiler.codemap.CodeMap;
import com.neaterbits.compiler.codemap.TypeInfo;
import com.neaterbits.compiler.types.MethodInfo;
import com.neaterbits.compiler.types.TypeVariant;
import com.neaterbits.compiler.util.model.CrossReferenceGetters;

public interface CompilerCodeMap extends CodeMap, CrossReferenceUpdater, CrossReferenceGetters, CompilerCodeMapGetters {

	int addFile(String file, int [] types);

	void addTypeMapping(TypeName name, int typeNo);

    Integer getTypeNoByTypeName(TypeName typeName);

    TypeName getTypeName(int typeNo);
    
    TypesMap<TypeName> makeTypesMap();

    void removeFile(String file);

	default int addType(TypeVariant typeVariant, int numMethods, int [] thisExtendsFromClasses, int [] thisExtendsFromInterfaces) {

		final int typeNo = addType(typeVariant, thisExtendsFromClasses, thisExtendsFromInterfaces);

		setMethodCount(typeNo, numMethods);

		return typeNo;
	}

    default MethodInfo getMethodInfo(TypeName typeName, String methodName, TypeName [] parameterTypes) {

        return getMethodInfo(getTypeNoByTypeName(typeName), methodName, parameterTypes);
    }

    default MethodInfo getMethodInfo(int typeNo, String methodName, TypeName [] parameterTypes) {

        final int [] parameterTypeNos = new int[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; ++ i) {
            parameterTypeNos[i] = getTypeNoByTypeName(parameterTypes[i]);
        }

        return getMethodInfo(typeNo, methodName, parameterTypeNos);
    }

    default int getClassThisExtendsFrom(int typeNo) {
		return getExtendsFromSingleSuperClass(typeNo);
	}

	default TypeInfo getClassExtendsFromTypeInfo(TypeName typeName) {

	    return getTypeInfo(getExtendsFromSingleSuperClass(getTypeNoByTypeName(typeName)));
	}

    default TypeInfo getTypeInfo(TypeName typeName) {

	    final Integer typeNo = getTypeNoByTypeName(typeName);

        return typeNo != null
                ? new TypeInfo(typeNo, getTypeVariantForType(typeNo))
                : null;
    }

    default TypeInfo getTypeInfo(int typeNo) {

        return new TypeInfo(typeNo, getTypeVariantForType(typeNo));
    }
}
