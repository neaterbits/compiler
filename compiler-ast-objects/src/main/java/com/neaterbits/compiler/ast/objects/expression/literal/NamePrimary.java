package com.neaterbits.compiler.ast.objects.expression.literal;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.expression.ExpressionVisitor;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class NamePrimary extends Primary {

    private final String name;
    
    public NamePrimary(Context context, String name) {
        super(context);
        
        Objects.requireNonNull(name);
        
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
        return visitor.onNamePrimary(this, param);
    }

    @Override
    public TypeReference getType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ParseTreeElement getParseTreeElement() {
        return ParseTreeElement.NAME_PRIMARY;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
        
    }
    
}
