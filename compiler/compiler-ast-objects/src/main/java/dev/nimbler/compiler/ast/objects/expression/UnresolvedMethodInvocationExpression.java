package dev.nimbler.compiler.ast.objects.expression;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.block.MethodName;
import dev.nimbler.compiler.types.ParseTreeElement;

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
