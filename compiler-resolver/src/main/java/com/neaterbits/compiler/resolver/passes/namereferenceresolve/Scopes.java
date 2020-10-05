package com.neaterbits.compiler.resolver.passes.namereferenceresolve;

import java.util.Objects;

import com.neaterbits.util.ArrayStack;
import com.neaterbits.util.Stack;

class Scopes {

	private final Stack<VariableScope> stack;
	
	Scopes() {
		this.stack = new ArrayStack<>();
	}
	
	VariableScope getCurrentScope() {
		return stack.get();
	}
	
	void push() {
		stack.push(new VariableScope());
	}
	
	void pop() {
		stack.pop();
	}
	
	VariableDeclaration resolveVariableDeclaration(String name) {
		
		Objects.requireNonNull(name);
		
		for (int i = stack.size() - 1; i >= 0; --i) {
			
			final VariableDeclaration declaration = stack.get(i).getVariableDeclaration(name);
			
			if (declaration != null) {
				return declaration;
			}
		}
		
		return null;
	}
}