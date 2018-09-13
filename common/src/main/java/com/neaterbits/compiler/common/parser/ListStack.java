package com.neaterbits.compiler.common.parser;

import java.util.Objects;

import com.neaterbits.compiler.common.Stack;
import com.neaterbits.compiler.common.ast.BaseASTElement;

public final class ListStack<T extends BaseASTElement, E extends ListStackEntry<T>> extends Stack<E> {

	public void addElement(T element) {
		
		Objects.requireNonNull(element);
		
		get().add(element);
	}
}
