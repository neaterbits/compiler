package com.neaterbits.compiler.util.model;

import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.TypeName;

public interface CompiledAndMappedFiles extends CompiledAndResolvedFiles {

	Integer getTypeNo(TypeName type);

	FieldInfo getFieldInfo(TypeName type, String fieldName);

	MethodInfo getMethodInfo(TypeName type, String methodName, TypeName [] parameterTypes);
	
	int getSourceFileNo(CompiledAndResolvedFile file);

	int getSourceFileNo(FileSpec fileSpec);

	int addToken(int sourceFile, int parseTreeRef);

}
