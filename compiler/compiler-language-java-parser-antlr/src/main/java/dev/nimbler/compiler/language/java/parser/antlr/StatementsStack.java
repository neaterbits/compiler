package dev.nimbler.compiler.language.java.parser.antlr;

import java.util.Objects;

import org.antlr.v4.runtime.Token;
import org.jutils.ArrayStack;

final class StatementsStack {

	private final ArrayStack<JavaStatements> stack;

	StatementsStack() {
		this.stack = new ArrayStack<>();
	}
	
	void push() {
		stack.push(new JavaStatements());
	}


	void add(JavaStatement statement, Token ... tokens) {
		Objects.requireNonNull(statement);
		
		stack.get().add(new JavaStatementHolder(statement, tokens));
	}

	JavaStatementHolder getLastFromTopFrame() {
		return stack.get().getLast();
	}

	JavaStatementHolder getLastFromFrame(int frame) {
		return stack.getFromTop(frame).getLast();
	}
	
	int getSizeOfFrame(int frame) {
		return stack.getFromTop(frame).size();
	}

	JavaStatementHolder getLastFromFrame(int frame, int fromLast) {
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
