package com.neaterbits.compiler.bytecode.common.loader;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.FieldType;
import com.neaterbits.compiler.common.resolver.codemap.CodeMap;

final class LoaderMaps {

	final CodeMap codeMap;
	final IntegerTypeMap typeMap;
	final LoadedClassesCache loadedClasses;

	LoaderMaps(CodeMap codeMap, IntegerTypeMap typeMap, LoadedClassesCache loadedClasses) {

		Objects.requireNonNull(codeMap);
		Objects.requireNonNull(typeMap);
		Objects.requireNonNull(loadedClasses);
		
		this.codeMap = codeMap;
		this.typeMap = typeMap;
		this.loadedClasses = loadedClasses;
	}
	
	int [] getParameters(List<FieldType> parameterTypes) {
		
		final int [] parameters = new int[parameterTypes.size()];
		
		for (int i = 0; i < parameters.length; ++ i) {
			parameters[i] = typeMap.getTypeNo(parameterTypes.get(i));
			
			if (parameters[i] < 0) {
				throw new IllegalStateException();
			}
		}

		return parameters;
	}
	
	int getReturnType(FieldType returnTypeName) {
		
		final int returnType = typeMap.getTypeNo(returnTypeName);
		
		if (returnType < 0) {
			throw new IllegalStateException();
		}

		return returnType;
	}

}
