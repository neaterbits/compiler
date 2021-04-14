package dev.nimbler.compiler.types.operator;

public enum Scope implements Operator {

    NAMES_SEPARATOR(Arity.BINARY);
    
    private final Arity arity;

    private Scope(Arity arity) {
        this.arity = arity;
    }

    @Override
    public OperatorType getOperatorType() {
        return OperatorType.SCOPE;
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
        return visitor.onScope(this, param);
    }
}
