package com.neaterbits.compiler.resolver.passes;

import java.util.Objects;

import com.neaterbits.util.ArrayStack;
import com.neaterbits.util.Stack;

public final class Scopes<T extends ScopeVariableDeclaration> {

	private final Stack<VariableScope<T>> stack;
	
	Scopes() {
		this.stack = new ArrayStack<>();
	}
	
	VariableScope<T> getCurrentScope() {
		return stack.get();
	}
	
	void push() {
		stack.push(new VariableScope<>());
	}
	
	void pop() {
		stack.pop();
	}
	
	T resolveVariableDeclaration(String name) {
		
		Objects.requireNonNull(name);
		
		for (int i = stack.size() - 1; i >= 0; --i) {
			
			final T declaration = stack.get(i).getVariableDeclaration(name);
			
			if (declaration != null) {
				return declaration;
			}
		}
		
		return null;
	}
}