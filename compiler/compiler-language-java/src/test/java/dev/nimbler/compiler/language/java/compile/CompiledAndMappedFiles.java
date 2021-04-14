package dev.nimbler.compiler.language.java.compile;

import dev.nimbler.compiler.language.java.TestFile;
import dev.nimbler.language.codemap.FieldInfo;
import dev.nimbler.language.common.types.TypeName;

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
