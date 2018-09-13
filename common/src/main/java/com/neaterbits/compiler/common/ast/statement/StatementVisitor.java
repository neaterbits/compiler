package com.neaterbits.compiler.common.ast.statement;

public interface StatementVisitor<T, R> {

	R onIf(IfStatement statement, T param);

	R onWhile(WhileStatement statement, T param);

	R onDoWhile(DoWhileStatement statement, T param);

	R onCFor(CForStatement statement, T param);

	R onAssignment(AssignmentStatement statement, T param);
}
