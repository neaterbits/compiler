package com.neaterbits.compiler.util.operator;

public enum IncrementDecrement implements Operator {

    PRE_INCREMENT(Arity.UNARY, Notation.PREFIX),
    PRE_DECREMENT(Arity.UNARY, Notation.PREFIX),
    POST_INCREMENT(Arity.UNARY, Notation.POSTFIX),
    POST_DECREMENT(Arity.UNARY, Notation.POSTFIX);
    
    private final Arity arity;
    private final Notation notation;

    private IncrementDecrement(Arity arity, Notation notation) {
        this.arity = arity;
        this.notation = notation;
    }

    @Override
    public OperatorType getOperatorType() {
        return OperatorType.INCREMENT_DECREMENT;
    }

    @Override
    public Enum<?> getEnumValue() {
        return this;
    }

    @Override
    public Arity getArity() {
        return arity;
    }

    public Notation getNotation() {
        return notation;
    }

    @Override
    public <T, R> R visit(OperatorVisitor<T, R> visitor, T param) {
        
        return visitor.onIncrementDecrement(this, param);
    }
}
