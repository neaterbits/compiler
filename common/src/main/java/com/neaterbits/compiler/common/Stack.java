package com.neaterbits.compiler.common;

import java.util.ArrayList;

public class Stack<T> {

	private final ArrayList<T> list;
	
	public Stack() {
		this.list = new ArrayList<>();
	}
	
	public final void push(T element) {
		if (element == null) {
			throw new IllegalArgumentException("element == null");
		}
		
		list.add(element);
	}

	public final T pop() {
		return list.remove(list.size() - 1);
	}
	
	public final T get() {
		return list.get(list.size() - 1);
	}
	
	public final boolean isEmpty() {
		return list.isEmpty();
	}
}
