package com.neaterbits.compiler.ast.list;

@FunctionalInterface
public interface ASTListIndexIterator<T> {

	void each(T element, int index);
	
}
