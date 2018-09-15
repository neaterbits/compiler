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

	public final T getFromTop(int count) {
		return list.get(list.size() - count - 1);
	}

	@SuppressWarnings("unchecked")
	public final <E extends T> E get(Class<E> cl) {
		for (int i = list.size() - 1; i >= 0; -- i) {
			final T element = list.get(i);
			
			if (element.getClass().equals(cl)) {
				return (E)element;
			}
		}

		return null;
	}

	public final boolean isEmpty() {
		return list.isEmpty();
	}
}
