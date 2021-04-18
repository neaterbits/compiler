package dev.nimbler.compiler.resolver.passes;

import java.util.Objects;

import org.jutils.ArrayStack;
import org.jutils.Stack;

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