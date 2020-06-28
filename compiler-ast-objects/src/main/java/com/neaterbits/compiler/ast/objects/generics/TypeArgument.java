package com.neaterbits.compiler.ast.objects.generics;

import java.util.Collection;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.util.Context;

public abstract class TypeArgument extends BaseASTElement {

    private final ASTList<TypeBound> typeBounds;
    
    TypeArgument(Context context, Collection<TypeBound> typeBounds) {
        super(context);
        
        this.typeBounds = makeList(typeBounds);
    }

    public final ASTList<TypeBound> getTypeBounds() {
        return typeBounds;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
    
        doIterate(typeBounds, recurseMode, iterator);
    }
}
