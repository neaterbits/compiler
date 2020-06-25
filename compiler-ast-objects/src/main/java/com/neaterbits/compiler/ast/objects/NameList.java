package com.neaterbits.compiler.ast.objects;

import java.util.List;

import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class NameList extends BaseASTElement {

    private final ASTList<Name> names;
    
    public NameList(Context context, List<Name> names) {
        super(context);

        this.names = makeList(names);
    }

    public ASTList<Name> getNames() {
        return names;
    }

    @Override
    public ParseTreeElement getParseTreeElement() {
        return ParseTreeElement.NAME_LIST;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(names, recurseMode, iterator);
    }
}
