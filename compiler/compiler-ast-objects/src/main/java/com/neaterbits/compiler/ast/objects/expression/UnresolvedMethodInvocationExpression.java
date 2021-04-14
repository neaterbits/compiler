package com.neaterbits.compiler.ast.objects.expression;

import com.neaterbits.compiler.ast.objects.block.MethodName;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.parse.context.Context;

public final class UnresolvedMethodInvocationExpression
        extends UnresolvedCall<MethodName> {

    public UnresolvedMethodInvocationExpression(Context context, MethodName callable, ParameterList parameters) {
        super(context, callable, parameters);
    }

    @Override
    public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
        return visitor.onUnresolvedMethodInvocation(this, param);
    }

    @Override
    public ParseTreeElement getParseTreeElement() {
        return ParseTreeElement.UNRESOLVED_METHOD_INVOCATION_EXPRESSION;
    }
}
