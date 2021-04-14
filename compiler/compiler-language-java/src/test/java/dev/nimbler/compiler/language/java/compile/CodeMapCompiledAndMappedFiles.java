package dev.nimbler.compiler.language.java.compile;

import dev.nimbler.compiler.language.java.TestFile;
import dev.nimbler.language.codemap.FieldInfo;
import dev.nimbler.language.codemap.compiler.CompilerCodeMap;
import dev.nimbler.language.common.types.TypeName;

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
