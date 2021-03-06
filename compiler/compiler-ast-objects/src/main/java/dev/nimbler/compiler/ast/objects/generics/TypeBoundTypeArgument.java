package dev.nimbler.compiler.ast.objects.generics;

import java.util.Collection;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.list.ASTList;

public abstract class TypeBoundTypeArgument extends TypeArgument {

    private final ASTList<TypeBound> typeBounds;
    
    TypeBoundTypeArgument(Context context, Collection<TypeBound> typeBounds) {
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
