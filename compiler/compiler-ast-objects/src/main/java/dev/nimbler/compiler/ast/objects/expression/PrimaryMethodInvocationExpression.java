package dev.nimbler.compiler.ast.objects.expression;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.block.MethodName;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.types.method.MethodInvocationType;

@Deprecated
public final class PrimaryMethodInvocationExpression extends ResolvedMethodInvocationExpression {

    private final ASTSingle<Expression> object;

    public PrimaryMethodInvocationExpression(Context context, Expression object, MethodName callable, ParameterList parameters) {
        super(
                context,
                MethodInvocationType.PRIMARY,
                callable,
                parameters);
        
        Objects.requireNonNull(object);
        
        this.object = makeSingle(object);
    }

    public Expression getObject() {
        return object.get();
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(object, recurseMode, iterator);
        
        super.doRecurse(recurseMode, iterator);
    }
}
