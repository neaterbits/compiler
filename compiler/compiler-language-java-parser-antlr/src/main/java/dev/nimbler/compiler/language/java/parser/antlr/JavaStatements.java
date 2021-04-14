package dev.nimbler.compiler.language.java.parser.antlr;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

final class JavaStatements {
	private final List<JavaStatementHolder> statements;

	JavaStatements() {
		this.statements = new ArrayList<>();
	}
	
	void add(JavaStatementHolder statement) {
		Objects.requireNonNull(statement);
		
		statements.add(statement);
	}

	JavaStatementHolder getLast() {
		return statements.get(statements.size() - 1);
	}

	JavaStatementHolder getLast(int fromLast) {
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
