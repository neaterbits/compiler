package com.neaterbits.compiler.common.ast.list;

@FunctionalInterface
public interface ASTListIndexIterator<T> {

	void each(T element, int index);
	
}
