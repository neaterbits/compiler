package com.neaterbits.compiler.common.parser;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Stack;
import com.neaterbits.compiler.common.ast.BaseASTElement;

public final class ListStack extends Stack<StackEntry> {

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
