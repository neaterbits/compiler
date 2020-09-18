package com.neaterbits.compiler.ast.objects.generics;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Context;

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
