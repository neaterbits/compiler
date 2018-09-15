package com.neaterbits.compiler.common.ast.operator;

public interface Operator {

	public <T, R> R visit(OperatorVisitor<T, R> visitor, T param);

}
