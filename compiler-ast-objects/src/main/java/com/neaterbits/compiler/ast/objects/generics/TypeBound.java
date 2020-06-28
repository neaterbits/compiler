package com.neaterbits.compiler.ast.objects.generics;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.typedefinition.TypeBoundType;

public final class TypeBound extends BaseASTElement {

    private final TypeBoundType type;
    private final ASTSingle<TypeReference> typeReference;
    
    public TypeBound(Context context, TypeBoundType type, TypeReference typeReference) {
        super(context);
    
        Objects.requireNonNull(type);
        Objects.requireNonNull(typeReference);
        
        this.type = type;
        this.typeReference = makeSingle(typeReference);
    }

    public TypeBoundType getType() {
        return type;
    }

    public TypeReference getTypeReference() {
        return typeReference.get();
    }

    @Override
    public ParseTreeElement getParseTreeElement() {
        
        return ParseTreeElement.TYPE_BOUND;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
        
        doIterate(typeReference, recurseMode, iterator);
    }
}
