package com.neaterbits.compiler.model.encoded;

import java.util.List;

import com.neaterbits.compiler.ast.encoded.AST;
import com.neaterbits.compiler.ast.encoded.ASTBufferRead;
import com.neaterbits.compiler.ast.encoded.ASTBufferRead.ParseTreeElementRef;
import com.neaterbits.compiler.ast.encoded.EncodedCompilationUnit;
import com.neaterbits.compiler.model.common.BaseImportsModel;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.types.imports.TypeImport;
import com.neaterbits.compiler.types.imports.TypeImportVisitor;

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
