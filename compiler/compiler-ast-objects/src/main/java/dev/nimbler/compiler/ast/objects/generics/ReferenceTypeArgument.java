package dev.nimbler.compiler.ast.objects.generics;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class ReferenceTypeArgument extends TypeArgument {

    private final ASTSingle<TypeReference> typeReference;
    
    public ReferenceTypeArgument(Context context, TypeReference typeReference) {
        super(context);
        
        Objects.requireNonNull(typeReference);
        
        this.typeReference = makeSingle(typeReference);
    }

    public TypeReference getTypeReference() {
        return typeReference.get();
    }

    @Override
    public ParseTreeElement getParseTreeElement() {
        return ParseTreeElement.REFERENCE_GENERIC_TYPE_ARGUMENT;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
        
        doIterate(typeReference, recurseMode, iterator);
    }
}
