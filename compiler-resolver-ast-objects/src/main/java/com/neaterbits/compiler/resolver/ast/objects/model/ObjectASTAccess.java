package com.neaterbits.compiler.resolver.ast.objects.model;

import java.util.Arrays;

import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.Namespace;
import com.neaterbits.compiler.ast.objects.typedefinition.ComplexTypeDefinition;
import com.neaterbits.compiler.ast.objects.typereference.BuiltinTypeReference;
import com.neaterbits.compiler.ast.objects.typereference.UnresolvedTypeReference;
import com.neaterbits.compiler.resolver.util.SourceTokenUtil.ASTAccess;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class ObjectASTAccess implements ASTAccess<BaseASTElement, CompilationUnit> {

    @Override
    public ParseTreeElement getParseTreeElement(BaseASTElement element, CompilationUnit compilationUnit) {

        return element.getParseTreeElement();
    }

    @Override
    public int getParseTreeRef(BaseASTElement element, CompilationUnit compilationUnit) {
        return compilationUnit.getParseTreeRefFromElement(element);
    }

    @Override
    public Context getContext(BaseASTElement element, CompilationUnit compilationUnit) {

        return element.getContext();
    }

    @Override
    public TypeName getBuiltinTypeReferenceTypeName(BaseASTElement element, CompilationUnit compilationUnit) {

        final BuiltinTypeReference builtinTypeReference = (BuiltinTypeReference)element;

        return builtinTypeReference.getTypeName();
    }

    @Override
    public ScopedName getResolveLaterReference(BaseASTElement element, CompilationUnit compilationUnit) {
        
        final UnresolvedTypeReference typeReference = (UnresolvedTypeReference)element;

        return typeReference.getScopedName();
    }

    @Override
    public ScopedName getReferencedFrom(BaseASTElement element, CompilationUnit compilationUnit) {

        final Namespace namespace = (Namespace)compilationUnit.findElement(false, e -> e instanceof Namespace);
        final ComplexTypeDefinition<?, ?> definition = (ComplexTypeDefinition<?, ?>)compilationUnit.findElement(false, e -> e instanceof ComplexTypeDefinition<?, ?>);
        
        final ScopedName referencedFrom;
        
        if (definition != null) {
            referencedFrom = new ScopedName(
                namespace != null
                    ? Arrays.asList(namespace.getParts())
                    : null,
                definition.getNameString());
        }
        else {
            referencedFrom = null;
        }
        
        return referencedFrom;
    }

    @Override
    public boolean isPlaceholderElement(BaseASTElement element, CompilationUnit compilationUnit) {

        return element.isPlaceholderElement();
    }
}
