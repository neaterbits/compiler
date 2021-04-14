package dev.nimbler.compiler.model.encoded;

import java.util.List;

import dev.nimbler.compiler.ast.encoded.AST;
import dev.nimbler.compiler.ast.encoded.ASTBufferRead;
import dev.nimbler.compiler.ast.encoded.EncodedCompilationUnit;
import dev.nimbler.compiler.ast.encoded.ASTBufferRead.ParseTreeElementRef;
import dev.nimbler.compiler.model.common.BaseImportsModel;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.imports.TypeImport;
import dev.nimbler.compiler.types.imports.TypeImportVisitor;

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
