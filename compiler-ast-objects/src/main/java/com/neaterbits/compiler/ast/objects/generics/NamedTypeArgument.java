package com.neaterbits.compiler.ast.objects.generics;

import java.util.Collection;
import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.Name;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public class NamedTypeArgument extends TypeArgument {

    private final ASTSingle<Name> name;

    public NamedTypeArgument(Context context, Name name, Collection<TypeBound> typeBounds) {
        super(context, typeBounds);
        
        Objects.requireNonNull(name);
        
        this.name = makeSingle(name);
    }

    public Name getName() {
        return name.get();
    }

    public String getNameString() {
        return name.get().getText();
    }

    @Override
    public ParseTreeElement getParseTreeElement() {

        return ParseTreeElement.NAMED_GENERIC_TYPE;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
        
        doIterate(name, recurseMode, iterator);
        
        super.doRecurse(recurseMode, iterator);
    }

}
