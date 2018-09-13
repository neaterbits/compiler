package com.neaterbits.compiler.common.ast.condition;

public interface ConditionVisitor<T, R> {

	R onNestedCondition(NestedCondition condition, T param);
	
}
