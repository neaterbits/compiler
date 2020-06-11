package com.neaterbits.compiler.resolver.ast.encoded.model;

import java.util.List;

import com.neaterbits.compiler.ast.encoded.EncodedCompilationUnit;
import com.neaterbits.compiler.parser.listener.encoded.AST;
import com.neaterbits.compiler.parser.listener.encoded.ASTBufferRead;
import com.neaterbits.compiler.parser.listener.encoded.ASTBufferRead.ParseTreeElementRef;
import com.neaterbits.compiler.util.imports.TypeImport;
import com.neaterbits.compiler.util.model.BaseImportsModel;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.model.TypeImportVisitor;

public class EncodedImportsModel extends BaseImportsModel<EncodedCompilationUnit> {

    public EncodedImportsModel(List<TypeImport> implicitImports) {
        super(implicitImports);
    }

    @Override
    public void iterateTypeImports(EncodedCompilationUnit sourceFile, TypeImportVisitor visitor) {
        
        int index = 0;
        
        final ASTBufferRead buffer = sourceFile.getBuffer();
        
        final ParseTreeElementRef ref = new ParseTreeElementRef();
        
        for (;;) {
            
            buffer.getParseTreeElement(index, ref);
            
            if (ref.element == ParseTreeElement.IMPORT) {
                
            }
            
            index += ref.isStart
                    ? AST.sizeStart(ref.element, buffer, ref.index)
                    : AST.sizeEnd(ref.element);
        }
    }
}
