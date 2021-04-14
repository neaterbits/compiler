package com.neaterbits.compiler.parser.recursive.cached.expressions;

import java.util.Objects;

import com.neaterbits.compiler.types.operator.Operator;

public final class CachedOperator {

    private int context;
    private Operator operator;
    private int precedence;

    CachedOperator() {
        
    }
    
    void init(int context, Operator operator, int precedence) {

        Objects.requireNonNull(operator);
        
        this.context = context;
        this.operator = operator;
        this.precedence = precedence;
    }

    public int getContext() {
        return context;
    }

    public Operator getOperator() {
        return operator;
    }

    public int getPrecedence() {
        return precedence;
    }

    @Override
    public String toString() {
        return operator.toString();
    }
}
