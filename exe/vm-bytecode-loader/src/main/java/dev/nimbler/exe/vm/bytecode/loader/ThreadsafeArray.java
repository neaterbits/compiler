package dev.nimbler.exe.vm.bytecode.loader;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public final class ThreadsafeArray<T> {

	private final AtomicReference<T[]> array;
	
	public ThreadsafeArray(T [] initialArray) {

		Objects.requireNonNull(initialArray);
		
		this.array = new AtomicReference<>(initialArray);
	}
	
	public void set(int index, T object) {
		throw new UnsupportedOperationException();
	}
	
	public T get(int index) {
		return array.get()[index];
	}
}
