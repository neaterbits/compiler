package com.neaterbits.compiler.java;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.types.FieldInfo;
import com.neaterbits.compiler.util.FileSpec;

public interface CompiledAndMappedFiles extends CompiledAndResolvedFiles {

	Integer getTypeNo(TypeName typeName);

	FieldInfo getFieldInfo(TypeName typeName, String fieldName);

	/*
	MethodInfo getMethodInfo(TypeName type, String methodName, TypeName [] parameterTypes);
	
	int getSourceFileNo(CompiledAndResolvedFile file);
	*/

	int getSourceFileNo(FileSpec fileSpec);
	
	/*
	int addToken(int sourceFile, int parseTreeRef);
	*/
}
