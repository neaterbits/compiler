package dev.nimbler.compiler.ast.objects.generics;

import java.util.Collection;
import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.ast.objects.Name;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.types.ParseTreeElement;

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
