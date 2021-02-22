package com.neaterbits.compiler.common;

import java.util.Objects;

public class StackDelegator<T> implements Stack<T> {

	private final Stack<T> delegate;

	public StackDelegator(Stack<T> delegate) {
		
		Objects.requireNonNull(delegate);
		
		this.delegate = delegate;
	}

	@Override
	public T get() {
		return delegate.get();
	}

	@Override
	public T get(int index) {
		return delegate.get(index);
	}

	@Override
	public void push(T element) {
		delegate.push(element);
	}

	@Override
	public T getFromTop(int count) {
		return delegate.getFromTop(count);
	}

	@Override
	public T pop() {
		return delegate.pop();
	}

	@Override
	public <E extends T> E getFromTop(Class<E> cl) {
		return delegate.getFromTop(cl);
	}

	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	@Override
	public int size() {
		return delegate.size();
	}
}
