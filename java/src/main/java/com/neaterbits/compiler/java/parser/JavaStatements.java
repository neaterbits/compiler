package com.neaterbits.compiler.java.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

final class JavaStatements {
	private final List<JavaStatement> statements;

	JavaStatements() {
		this.statements = new ArrayList<>();
	}
	
	void add(JavaStatement statement) {
		Objects.requireNonNull(statement);
		
		statements.add(statement);
	}

	JavaStatement getLast() {
		return statements.get(statements.size() - 1);
	}

	JavaStatement getLast(int fromLast) {
		return statements.get(statements.size() - fromLast - 1);
	}
	
	int size() {
		return statements.size();
	}

	@Override
	public String toString() {
		return statements.toString();
	}
}
