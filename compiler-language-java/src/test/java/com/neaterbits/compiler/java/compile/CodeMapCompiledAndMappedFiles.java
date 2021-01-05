package com.neaterbits.compiler.java.compile;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.java.TestFile;
import com.neaterbits.compiler.types.FieldInfo;

public interface CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> extends CompiledAndMappedFiles {

    /*
	CompilerCodeMapGetters getCompilerCodeMap();
	
	CrossReferenceUpdater getCrossReferenceUpdater();
    */
	
 	COMPILATION_UNIT getCompilationUnit(String name);

 	default COMPILATION_UNIT getCompilationUnit(TestFile testFile) {
 	    
 	    return getCompilationUnit(testFile.getName());
 	}

 	default FieldInfo getFieldInfo(CompilerCodeMap codeMap, TypeName typeName, String fieldName) {
        
        final Integer typeNo = codeMap.getTypeNoByTypeName(typeName);
        
        if (typeNo == null) {
            throw new IllegalStateException("No type for " + typeName);
        }
        
        return codeMap.getFieldInfo(typeNo, fieldName);
    }
}
