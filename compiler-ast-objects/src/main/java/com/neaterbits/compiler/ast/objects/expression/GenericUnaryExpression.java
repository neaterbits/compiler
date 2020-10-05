package com.neaterbits.compiler.ast.objects.expression;

import java.util.Objects;

import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.types.operator.Arity;
import com.neaterbits.compiler.types.operator.Operator;
import com.neaterbits.util.parse.context.Context;

public final class GenericUnaryExpression extends UnaryExpression {

    private final Operator operator;
    private final ParseTreeElement parseTreeElement;

    public GenericUnaryExpression(Context context, Operator operator, ParseTreeElement parseTreeElement, Expression expression) {
        super(context, expression);

        Objects.requireNonNull(operator);
        Objects.requireNonNull(parseTreeElement);
        
        if (operator.getArity() != Arity.UNARY) {
            throw new IllegalArgumentException();
        }

        this.operator = operator;
        this.parseTreeElement = parseTreeElement;
    }

    public final Operator getOperator() {
        return operator;
    }

    @Override
    public ParseTreeElement getParseTreeElement() {
        return parseTreeElement;
    }

    @Override
    public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
        throw new UnsupportedOperationException();
    }
}
