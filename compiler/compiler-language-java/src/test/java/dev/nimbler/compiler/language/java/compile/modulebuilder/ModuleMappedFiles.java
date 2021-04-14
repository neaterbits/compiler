package dev.nimbler.compiler.language.java.compile.modulebuilder;

import java.util.Map;
import java.util.Objects;

import dev.nimbler.compiler.model.common.passes.ParsedFiles;
import dev.nimbler.compiler.util.parse.ParsedFile;
import dev.nimbler.language.codemap.FieldInfo;
import dev.nimbler.language.codemap.compiler.CompilerCodeMap;
import dev.nimbler.language.common.types.TypeName;

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
