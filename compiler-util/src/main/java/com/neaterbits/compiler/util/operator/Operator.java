package com.neaterbits.compiler.util.operator;

public interface Operator {

    public OperatorType getOperatorType();
    
    public Enum<?> getEnumValue();
    
	public Arity getArity();
	
	public <T, R> R visit(OperatorVisitor<T, R> visitor, T param);

}
