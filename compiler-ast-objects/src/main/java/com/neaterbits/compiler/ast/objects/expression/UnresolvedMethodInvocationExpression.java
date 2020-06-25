package com.neaterbits.compiler.ast.objects.expression;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.NameList;
import com.neaterbits.compiler.ast.objects.block.MethodName;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.method.MethodInvocationType;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class UnresolvedMethodInvocationExpression extends Call<MethodName> {

    private final MethodInvocationType type;
    private final ASTSingle<NameList> nameList;
    
    public UnresolvedMethodInvocationExpression(
            Context context,
            MethodInvocationType type,
            NameList nameList,
            MethodName callable,
            ParameterList parameters) {
        
        super(context, callable, parameters);
        
        Objects.requireNonNull(type);
        Objects.requireNonNull(nameList);
        
        this.type = type;
        this.nameList = makeSingle(nameList);
    }
    
    public MethodInvocationType getInvocationType() {
        return type;
    }

    public NameList getNameList() {
        return nameList.get();
    }

    @Override
    public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {

        throw new UnsupportedOperationException("Called before resolved");
    }

    @Override
    public ParseTreeElement getParseTreeElement() {

        return ParseTreeElement.UNRESOLVED_METHOD_INVOCATION_EXPRESSION;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(nameList, recurseMode, iterator);
        
        super.doRecurse(recurseMode, iterator);
    }
}
