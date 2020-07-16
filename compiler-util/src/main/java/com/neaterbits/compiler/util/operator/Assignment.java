package com.neaterbits.compiler.util.operator;

public enum Assignment implements Operator {

    ASSIGN(Arity.BINARY);
    
    private final Arity arity;

    private Assignment(Arity arity) {
        this.arity = arity;
    }

    @Override
    public OperatorType getOperatorType() {
        return OperatorType.ASSIGNMENT;
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
        return visitor.onAssignment(this, param);
    }
}
