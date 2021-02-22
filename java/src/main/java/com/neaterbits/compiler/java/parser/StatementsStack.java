package com.neaterbits.compiler.java.parser;

import java.util.Objects;

import com.neaterbits.compiler.common.ArrayStack;

final class StatementsStack {

	private final ArrayStack<JavaStatements> stack;

	StatementsStack() {
		this.stack = new ArrayStack<>();
	}
	
	void push() {
		stack.push(new JavaStatements());
	}

	void add(JavaStatement statement) {
		Objects.requireNonNull(statement);
		
		stack.get().add(statement);
	}

	JavaStatement getLastFromTopFrame() {
		return stack.get().getLast();
	}

	JavaStatement getLastFromFrame(int frame) {
		return stack.getFromTop(frame).getLast();
	}
	
	int getSizeOfFrame(int frame) {
		return stack.getFromTop(frame).size();
	}

	JavaStatement getLastFromFrame(int frame, int fromLast) {
		return stack.getFromTop(frame).getLast(fromLast);
	}

	void pop() {
		stack.pop();
	}
	
	int size() {
		return stack.size();
	}

	@Override
	public String toString() {
		return stack.toString();
	}
}
