package com.neaterbits.compiler.model.objects;

import java.util.Arrays;

import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.Namespace;
import com.neaterbits.compiler.ast.objects.typedefinition.ComplexTypeDefinition;
import com.neaterbits.compiler.ast.objects.typereference.BuiltinTypeReference;
import com.neaterbits.compiler.ast.objects.typereference.UnresolvedTypeReference;
import com.neaterbits.compiler.model.common.SourceTokenUtil.ASTAccess;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.language.common.types.ScopedName;
import com.neaterbits.language.common.types.TypeName;
import com.neaterbits.util.parse.context.Context;

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
