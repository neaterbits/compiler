package com.neaterbits.compiler.java.compile;

import com.neaterbits.compiler.java.TestFile;
import com.neaterbits.language.codemap.FieldInfo;
import com.neaterbits.language.common.types.TypeName;

public interface CompiledAndMappedFiles extends CompiledAndResolvedFiles {

	Integer getTypeNo(TypeName typeName);

	FieldInfo getFieldInfo(TypeName typeName, String fieldName);

	/*
	MethodInfo getMethodInfo(TypeName type, String methodName, TypeName [] parameterTypes);
	
	int getSourceFileNo(CompiledAndResolvedFile file);
	*/

	int getSourceFileNo(String name);
	
	default int getSourceFileNo(TestFile testFile) {
	    
	    return getSourceFileNo(testFile.getName());
	}
	
	/*
	int addToken(int sourceFile, int parseTreeRef);
	*/
}
