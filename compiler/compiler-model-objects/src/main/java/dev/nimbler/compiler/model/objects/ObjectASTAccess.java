package dev.nimbler.compiler.model.objects;

import java.util.Arrays;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.ast.objects.CompilationUnit;
import dev.nimbler.compiler.ast.objects.Namespace;
import dev.nimbler.compiler.ast.objects.typedefinition.ComplexTypeDefinition;
import dev.nimbler.compiler.ast.objects.typereference.BuiltinTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.UnresolvedTypeReference;
import dev.nimbler.compiler.model.common.SourceTokenUtil.ASTAccess;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.language.common.types.ScopedName;
import dev.nimbler.language.common.types.TypeName;

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
