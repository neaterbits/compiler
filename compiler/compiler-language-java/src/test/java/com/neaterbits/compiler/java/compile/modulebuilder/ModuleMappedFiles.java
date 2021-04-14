package com.neaterbits.compiler.java.compile.modulebuilder;

import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.model.common.passes.ParsedFiles;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.language.codemap.FieldInfo;
import com.neaterbits.language.codemap.compiler.CompilerCodeMap;
import com.neaterbits.language.common.types.TypeName;

abstract class ModuleMappedFiles<PARSED_FILE extends ParsedFile, COMPILATION_UNIT>
    extends BaseMappedFiles<PARSED_FILE, COMPILATION_UNIT> {
        
    private final CompilerCodeMap codeMap;
    
    public ModuleMappedFiles(
            ParsedFiles<PARSED_FILE> other,
            Map<String, Integer> sourceFileNos,
            CompilerCodeMap codeMap) {
        
        super(other, sourceFileNos);
        
        Objects.requireNonNull(codeMap);
    
        this.codeMap = codeMap;
    }

    @Override
    public Integer getTypeNo(TypeName typeName) {
        
        Objects.requireNonNull(typeName);
        
        return codeMap.getTypeNoByTypeName(typeName);
    }
    
    @Override
    public FieldInfo getFieldInfo(TypeName typeName, String fieldName) {
        return getFieldInfo(codeMap, typeName, fieldName);
    }
}
