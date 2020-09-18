package com.neaterbits.compiler.types.operator;

public interface Operator {

    public OperatorType getOperatorType();
    
    public Enum<?> getEnumValue();
    
	public Arity getArity();
	
	public <T, R> R visit(OperatorVisitor<T, R> visitor, T param);

}
