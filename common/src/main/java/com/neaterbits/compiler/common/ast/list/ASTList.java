package com.neaterbits.compiler.common.ast.list;

import java.util.function.Predicate;

public abstract class ASTList<T extends ASTNode> extends ASTNodeHolder implements Iterable<T> {

	public abstract boolean isEmpty();
	
	public abstract int size();
	
	public abstract void foreachWithIndex(ASTListIndexIterator<T> function);
	// public abstract T get(int index);
	
	public abstract T find(Predicate<T> predicate);
}
