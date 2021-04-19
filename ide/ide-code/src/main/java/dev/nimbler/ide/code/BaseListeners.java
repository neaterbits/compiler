package dev.nimbler.ide.code;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class BaseListeners<T> {

	private final List<T> listeners;

	protected BaseListeners() {

		this.listeners = new ArrayList<>();
	}
	
	public final void addListener(T listener) {
		
		Objects.requireNonNull(listener);
		
		listeners.add(listener);
	}

	public final void removeListener(T listener) {
		
		Objects.requireNonNull(listener);
		
		listeners.remove(listener);
	}
	
	protected final void forEach(Consumer<T> function) {
		listeners.forEach(function);
	}
}
