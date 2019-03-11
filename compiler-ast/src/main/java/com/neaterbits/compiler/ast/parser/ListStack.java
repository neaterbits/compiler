package com.neaterbits.compiler.ast.parser;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.util.ArrayStack;

public final class ListStack extends ArrayStack<StackEntry> {

	public <T extends BaseASTElement> void addElement(T element) {
		
		Objects.requireNonNull(element);
		
		@SuppressWarnings("unchecked")
		final ListStackEntry<T> stackEntry = (ListStackEntry<T>)get();
		
		stackEntry.add(element);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BaseASTElement, L extends ListStackEntry<T>> L popListEntry(Class<T> type) {
		return (L)pop();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BaseASTElement> List<T> popList() {
		return ((ListStackEntry<T>)pop()).getList();
	}
}