package com.neaterbits.compiler.types.operator;

public enum Instantiation implements Operator {

    NEW(Arity.UNARY);
    
    private final Arity arity;

    private Instantiation(Arity arity) {
        this.arity = arity;
    }

    @Override
    public OperatorType getOperatorType() {
        return OperatorType.INSTANTIATION;
    }

    @Override
    public Enum<?> getEnumValue() {
        return this;
    }

    @Override
    public Arity getArity() {
        return arity;
    }

    @Override
    public <T, R> R visit(OperatorVisitor<T, R> visitor, T param) {

        return visitor.onInstantiation(this, param);
    }
}
