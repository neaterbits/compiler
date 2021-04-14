package dev.nimbler.compiler.parser.listener.stackbased.state.base;

import java.util.List;
import java.util.Objects;

import com.neaterbits.util.ArrayStack;

public final class ListStack extends ArrayStack<StackEntry> {

	public <T> void addElement(T element) {
		
		Objects.requireNonNull(element);
		
		@SuppressWarnings("unchecked")
		final ListStackEntry<T> stackEntry = (ListStackEntry<T>)get();
		
		stackEntry.add(element);
	}
	
	@SuppressWarnings("unchecked")
	public <T, L extends ListStackEntry<T>> L popListEntry(Class<T> type) {
		return (L)pop();
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> popList() {
		return ((ListStackEntry<T>)pop()).getList();
	}
}
