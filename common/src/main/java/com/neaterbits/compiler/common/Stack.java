package com.neaterbits.compiler.common;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Stack<T> implements StackView<T> {

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
	
	@Override
	public final T get() {
		return list.get(list.size() - 1);
	}
	
	@Override
	public final T get(int index) {
		return list.get(index);
	}
	
	public final Stream<T> stream() {
		return list.stream();
	}

	@Override
	public final T getFromTop(int count) {
		return list.get(list.size() - count - 1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final <E extends T> E getFromTop(Class<E> cl) {
		for (int i = list.size() - 1; i >= 0; -- i) {
			final T element = list.get(i);
			
			if (element.getClass().equals(cl)) {
				return (E)element;
			}
		}

		return null;
	}

	@Override
	public final boolean isEmpty() {
		return list.isEmpty();
	}
	
	@Override
	public final int size() {
		return list.size();
	}

	@Override
	public String toString() {
		return list.toString();
	}
}
