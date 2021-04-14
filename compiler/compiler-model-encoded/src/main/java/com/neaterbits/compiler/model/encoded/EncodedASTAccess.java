package com.neaterbits.compiler.model.encoded;

import com.neaterbits.compiler.ast.encoded.EncodedCompilationUnit;
import com.neaterbits.compiler.model.common.SourceTokenUtil.ASTAccess;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.language.common.types.ScopedName;
import com.neaterbits.language.common.types.TypeName;
import com.neaterbits.util.parse.context.Context;

final class EncodedASTAccess implements ASTAccess<Integer, EncodedCompilationUnit> {

    @Override
    public ParseTreeElement getParseTreeElement(Integer element, EncodedCompilationUnit compilationUnit) {

        return compilationUnit.getParseTreeElement(element);
    }

    @Override
    public int getParseTreeRef(Integer element, EncodedCompilationUnit compilationUnit) {
        return element;
    }

    @Override
    public Context getContext(Integer element, EncodedCompilationUnit compilationUnit) {
        
        return compilationUnit.getContextByStartElementRef(element);
    }

    @Override
    public TypeName getBuiltinTypeReferenceTypeName(Integer element, EncodedCompilationUnit compilationUnit) {

        return compilationUnit.getTypeName(element + 1);
    }

    @Override
    public ScopedName getResolveLaterReference(Integer element, EncodedCompilationUnit compilationUnit) {
        
        throw new UnsupportedOperationException();
        // return compilationUnit.getScopedName(element + 1);
    }

    @Override
    public ScopedName getReferencedFrom(Integer element, EncodedCompilationUnit compilationUnit) {
        // FIXME Auto-generated method stub
        return null;
    }

    @Override
    public boolean isPlaceholderElement(Integer element, EncodedCompilationUnit compilationUnit) {
        
        final ParseTreeElement parseTreeElement = compilationUnit.getParseTreeElement(element);

        return parseTreeElement.isPlaceholder();
    }
}
