package dev.nimbler.ide.refactor;

import java.util.Objects;

import dev.nimbler.language.codemap.compiler.CompilerCodeMap;
import dev.nimbler.language.common.types.TypeName;

public class IDERefactoring {
    
    /**
     * Rename a method, variable or other reference
     * that does not require file move.
     */
    public void rename(CompilerCodeMap codeMap, int crossReferenceToken) {
        
    }

    /**
     * A rename or move of a file (like 'mv')
     */
    public void moveFileType(CompilerCodeMap codeMap, TypeName type) {
        
        Objects.requireNonNull(codeMap);
        Objects.requireNonNull(type);
        
    }
}
