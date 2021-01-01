package com.neaterbits.compiler.ast.objects.expression;

import java.util.Objects;

import com.neaterbits.compiler.types.operator.Arity;
import com.neaterbits.compiler.types.operator.UnaryOperator;
import com.neaterbits.util.parse.context.Context;

public final class GenericUnaryExpression extends UnaryExpression {

    private final UnaryOperator operator;

    public GenericUnaryExpression(
            Context context,
            UnaryOperator operator,
            Expression expression) {
        
        super(context, expression);

        Objects.requireNonNull(operator);
        
        if (operator.getArity() != Arity.UNARY) {
            throw new IllegalArgumentException();
        }

        this.operator = operator;
    }

    public final UnaryOperator getOperator() {
        return operator;
    }

    @Override
    public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
        return visitor.onGenericUnaryExpression(this, param);
    }
}
