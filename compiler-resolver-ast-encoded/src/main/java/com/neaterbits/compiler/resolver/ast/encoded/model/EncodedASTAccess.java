package com.neaterbits.compiler.resolver.ast.encoded.model;

import com.neaterbits.compiler.ast.encoded.EncodedCompilationUnit;
import com.neaterbits.compiler.resolver.util.SourceTokenUtil.ASTAccess;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.ParseTreeElement;

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
