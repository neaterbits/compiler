package com.neaterbits.compiler.util;

public interface Stack<T> extends StackView<T> {

	void push(T element);
	
	T pop();
	
}
