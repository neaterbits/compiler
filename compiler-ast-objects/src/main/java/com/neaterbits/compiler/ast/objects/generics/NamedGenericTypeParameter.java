package com.neaterbits.compiler.ast.objects.generics;

import java.util.Collection;
import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.Name;
import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public class NamedGenericTypeParameter extends BaseASTElement {

    private final ASTSingle<Name> name;
    private final ASTList<TypeBound> typeBounds;

    public NamedGenericTypeParameter(Context context, Name name, Collection<TypeBound> typeBounds) {
        super(context);
        
        Objects.requireNonNull(name);
        
        this.name = makeSingle(name);
        this.typeBounds = makeList(typeBounds);
    }

    public Name getName() {
        return name.get();
    }

    public String getNameString() {
        return name.get().getText();
    }

    public ASTList<TypeBound> getTypeBounds() {
        return typeBounds;
    }

    @Override
    public ParseTreeElement getParseTreeElement() {

        return ParseTreeElement.NAMED_GENERIC_TYPE_PARAMETER;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
        
        doIterate(name, recurseMode, iterator);
        
        doIterate(typeBounds, recurseMode, iterator);
    }
}
