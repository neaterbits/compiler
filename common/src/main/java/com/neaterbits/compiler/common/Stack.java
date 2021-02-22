package com.neaterbits.compiler.common;

public interface Stack<T> extends StackView<T> {

	void push(T element);
	
	T pop();
	
}
