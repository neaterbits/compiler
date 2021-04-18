package dev.nimbler.compiler.ast.objects.expression.literal;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.expression.ExpressionVisitor;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.variables.UnresolvedPrimary;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class UnresolvedNamePrimary extends UnresolvedPrimary {

    private final String name;
    
    public UnresolvedNamePrimary(Context context, String name) {
        super(context);
        
        Objects.requireNonNull(name);
        
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
        return visitor.onUnresolvedNamePrimary(this, param);
    }

    @Override
    public TypeReference getType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ParseTreeElement getParseTreeElement() {
        return ParseTreeElement.UNRESOLVED_NAME_PRIMARY;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
        
    }
}
