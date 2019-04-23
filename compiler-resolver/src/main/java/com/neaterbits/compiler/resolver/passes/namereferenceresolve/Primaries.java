package com.neaterbits.compiler.resolver.passes.namereferenceresolve;

import java.util.Objects;

import com.neaterbits.compiler.util.ArrayStack;
import com.neaterbits.compiler.util.Stack;

class Primaries {

	private final Stack<PrimaryListEntry> primaryLists;
	
	Primaries() {
		this.primaryLists = new ArrayStack<>();
	}
	
	void push(PrimaryListEntry primaryListEntry) {
		
		Objects.requireNonNull(primaryListEntry);
		
		primaryLists.push(primaryListEntry);
	}
	
	void pop() {
		primaryLists.pop();
	}
	
	PrimaryListEntry getEntry() {
		return primaryLists.get(primaryLists.size() - 1);
	}
}